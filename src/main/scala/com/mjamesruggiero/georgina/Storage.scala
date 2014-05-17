package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.config._
import scalaz.Reader._

case class QueryException(message:String) extends Exception(message)

case class CategorySummary(category: String, count: Int, mean: Double, sttdev: Double)

object Storage {

  def initialize(env: String) = {
    DBsWithEnv(env).setupAll()
    ConnectionPool('default).borrow()
  }

  def storeLinesAsTransactions(env: String, lines: List[Line])(implicit session: DBSession = AutoSession) = {
    val transactions = lines.map { l => JSONParsers.buildTransaction(l) }
    val nonExistentTransactions = transactions.filter(! exists(env, _))
    nonExistentTransactions.map { t => store(env, t) }
  }

  def store(env: String, t: Transaction)(implicit session: DBSession = AutoSession) = {
    initialize(env)

    t match {
      case Transaction(id, date, species, amt, cat, desc) => {
        val validCat = new Categorizer(desc).categorize.c
        val validSpecies = if (amt < 0.0) "debit" else "asset"
        sql"""INSERT INTO transactions(id, date, species, description, category, amount)
              VALUES (null, ${date}, ${validSpecies}, ${desc}, ${validCat}, ${amt})""".execute.apply()
        true
      }
      case _ => false
    }
  }

  def exists(env: String, t: Transaction)(implicit session: DBSession = AutoSession): Boolean = {
    initialize(env)

    val returned: Option[Int] = sql"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date=${t.date}
          AND species=${t.species}
          AND description=${t.description}
          AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

      returned match {
        case Some(count) => count > 0
        case _ => false
    }
  }

  def all(env: String)(implicit session: DBSession = AutoSession): List[Transaction] = {
    initialize(env)

    sql"""SELECT id, date, species, amount, category, description
          FROM transactions ORDER BY date, amount DESC"""
    .map {
      rs => Transaction(
          rs.long("id"),
          DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("category"),
          rs.string("description")
        )
    }.list.apply()
  }

  def withCategory(env: String, category: String, start: DateTime, end: DateTime)(implicit session: DBSession = AutoSession): List[Transaction] = {
    initialize(env)

    sql"""SELECT id, date, species, amount, category, description
    FROM transactions
    WHERE category = ${category}
    ORDER BY date DESC"""
    .map {
      rs => Transaction(
          rs.long("id"),
          DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("category"),
          rs.string("description")
        )
    }.list.apply()
  }

  def inDateSpan(env: String, startDate: DateTime, endDate: DateTime)(implicit session: DBSession = AutoSession): List[Transaction] = {
    initialize(env)

    sql"""SELECT id, date, species, amount, category, description
    FROM transactions
    WHERE date >= ${startDate}
    AND date <= ${endDate}
    ORDER BY date DESC"""
    .map {
      rs => Transaction(
          rs.long("id"),
          DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("category"),
          rs.string("description")
        )
    }.list.apply()
  }

  def categoryStats(env: String,
                    startDate: DateTime,
                    endDate: DateTime)(implicit session: DBSession = AutoSession): List[CategorySummary] = {


    initialize(env)

    sql"""SELECT category, COUNT(*) category_count,
    AVG(amount) mean, STD(amount) stddev
    FROM transactions
    WHERE date >= ${startDate}
    AND date <= ${endDate}
    GROUP BY category
    ORDER by mean DESC"""
    .map {
      rs => CategorySummary(
          rs.string("category"),
          rs.int("category_count"),
          rs.double("mean"),
          rs.double("stddev")
        )
    }.list.apply()
  }
}
