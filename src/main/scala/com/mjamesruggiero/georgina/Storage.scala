package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.config._

case class QueryException(message:String) extends Exception(message)

object Storage {

  def initialize = {
    DBsWithEnv("development").setupAll()
    ConnectionPool('default).borrow()
  }

  def storeTransaction(t: Transaction)(implicit session: DBSession = AutoSession) = {
    initialize

    if(! existingAlready(t)) {
      t match {
        case Transaction(date, species, amt, desc) => {
          sql"""INSERT INTO transactions(id, date, species, description, amount)
                VALUES (null, ${date}, ${species}, ${desc}, ${amt})""".execute.apply()
        }
      }
    }
  }

  def existingAlready(t: Transaction)(implicit session: DBSession = AutoSession): Boolean = {
    initialize

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

  def allTransactions(implicit session: DBSession = AutoSession): List[Transaction] = {
    initialize

    sql"SELECT id, date, species, amount, description FROM transactions ORDER BY id"
    .map {
      rs => Transaction(DateTime.parse(rs.string("date")),
          rs.string("species"),
          rs.double("amount"),
          rs.string("description")
        )
    }.list.apply()
  }
}
