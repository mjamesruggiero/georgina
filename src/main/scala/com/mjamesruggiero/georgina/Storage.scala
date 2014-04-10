package com.mjamesruggiero.georgina

import scalikejdbc._
import SQLInterpolation._

object Storage {
  def storeTransaction(t: Transaction) = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
    implicit val session = AutoSession
    
    t match {
      case Transaction(date, species, amt, desc) => { sql"""INSERT INTO transactions(id, date, species, description, amount)
    VALUES (null, '${date}', '${species}', ${amt}, '${desc}')""".update.apply() }
      case _ => None
    }
  }
}

