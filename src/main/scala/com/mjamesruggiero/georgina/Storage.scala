package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.config._
import scalaz.Reader._

case class QueryException(message:String) extends Exception(message)

object Storage {

  def initialize(env: String) = {
    DBsWithEnv(env).setupAll()
    ConnectionPool('default).borrow()
  }

  def storeTransaction(env: String, t: Transaction)(implicit session: DBSession = AutoSession) = {
    initialize(env)

    if(! existingAlready(env, t)) {
      t match {
        case Transaction(date, species, amt, cat, desc) => {
          val newCat = new Categorizer(desc).categorize.c
          sql"""INSERT INTO transactions(id, date, species, description, category, amount)
                VALUES (null, ${date}, ${species}, ${desc}, ${newCat}, ${amt})""".execute.apply()
        }
      }
    }
  }

  def existingAlready(env: String, t: Transaction)(implicit session: DBSession = AutoSession): Boolean = {
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

  def allTransactions(env: String)(implicit session: DBSession = AutoSession): List[Transaction] = {
    initialize(env)

    sql"SELECT id, date, species, amount, description FROM transactions ORDER BY id"
    .map {
      rs => Transaction(DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("category"),
          rs.string("description")
        )
    }.list.apply()
  }
}
