package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import com.mjamesruggiero.georgina.config._
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scalikejdbc.config._
import scalaz.Reader._

case class QueryException(message:String) extends Exception(message)

case class CategorySummary(category: String, count: Int, mean: Double, sttdev: Double)

object Storage {

  import DB._
  lazy val format = DateTimeFormat.forPattern("yyyy-MM-dd");

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
        if (! exists(env, t)) {
          val validCat = new Categorizer(desc).categorize.c
          val validSpecies = if (amt < 0.0) "debit" else "asset"
          sql"""INSERT INTO transactions(id, date, species, description, category, amount)
                VALUES (null, ${date}, ${validSpecies}, ${desc}, ${validCat}, ${amt})""".execute.apply()
          true
        }
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
    AND date >= ${start}
    AND date <= ${end}
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

  def categoryStats(startDate: DateTime,
                    endDate: DateTime,
                    config: DBConfig): List[CategorySummary] = {

    val queryString = s"""SELECT category, COUNT(*) category_count,
    AVG(amount) mean, STD(amount) stddev
    FROM transactions
    WHERE date >= '${startDate.toString(format)}'
    AND date <= '${endDate.toString(format)}'
    GROUP BY category
    ORDER by category_count DESC"""
    val resultMap = Map(
      "category" -> mkString,
      "category_count" -> mkInt,
      "mean" -> mkDouble,
      "stddev" -> mkDouble
    )
    val result = query(queryString, resultMap, config) map { row =>
      CategorySummary(
          row.get("category").fold("")(asString),
          row.get("category_count").fold(0)(asInt),
          row.get("mean").fold(0.0)(asDouble),
          row.get("stddev").fold(0.0)(asDouble)
        )
    }
    result
  }

  def getById(env: String, id: Int)(implicit session: DBSession = AutoSession): Option[Transaction] = {
    initialize(env)

    sql"""SELECT id, date, species, amount, category, description
          FROM transactions WHERE id = ${id}"""
    .map {
      rs => Transaction(
          rs.long("id"),
          DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("category"),
          rs.string("description")
        )
    }.list.first.apply()
  }

  def update(env: String, t: Transaction)(implicit session: DBSession = AutoSession): Boolean = {
    t match {
      case Transaction(id, date, species, amt, cat, desc) => {
        initialize(env)
        val validCat = new Categorizer(desc).categorize.c
        val validSpecies = if (amt < 0.0) "debit" else "asset"
        sql"""UPDATE transactions SET
              date = ${date},
              species = ${validSpecies},
              description = ${desc},
              category = ${validCat},
              amount = ${amt}
              WHERE id = ${id}""".execute.apply()
        true
      }
      case _ => false
    }
  }

  def byWeek(config: DBConfig): List[DateSummary] = {
    val q = """SELECT FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7)) AS week_beginning,
          SUM(amount) AS total,
          COUNT(*) AS count
          FROM transactions
          WHERE species = 'debit'
          GROUP BY FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7))
          ORDER BY FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7)) DESC"""
    val result = query(q,
                      Map("week_beginning" -> mkString,
                          "total" -> mkDouble,
                          "count" -> mkInt), config) map { row =>
              DateSummary(
                DateTime.parse(row.get("week_beginning").fold("")(asString)),
                row.get("total").fold(0.0)(asDouble),
                row.get("count").fold(0)(asInt)
              )
            }
    result
  }
}
