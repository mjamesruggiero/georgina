package com.mjamesruggiero.georgina

import scalikejdbc._
import scalikejdbc.SQLInterpolation._
import scalikejdbc.config._

case class QueryException(message:String) extends Exception(message)

object Storage {

  def storeTransaction(t: Transaction)(implicit session: DBSession = AutoSession) = {
    DBsWithEnv("development").setupAll()
    ConnectionPool('default).borrow()

    t match {
      case Transaction(date, species, amt, desc) => {
        sql"""INSERT INTO transactions(id, date, species, description, amount)
              VALUES (null, ${date}, ${species}, ${desc}, ${amt})""".execute.apply()
      }
    }
  }
}

