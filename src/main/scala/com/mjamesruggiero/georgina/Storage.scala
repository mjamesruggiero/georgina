package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
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
}
