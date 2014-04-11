package com.mjamesruggiero.georgina

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

object Storage {
  def storeTransaction(t: Transaction): Boolean = {

    // put this in an implicit or pass it
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
    implicit val session = AutoSession

    // this is a placeholder until we install Mockito
    // bootstrap in-memory database
    sql"""CREATE TABLE transactions (
          id int(11) NOT NULL AUTO_INCREMENT,
          date date,
          species varchar(255) DEFAULT NULL,
          description text,
          amount float DEFAULT NULL,
          PRIMARY KEY (id));""".execute.apply()

    t match {
      case Transaction(date, species, amt, desc) => {
        sql"""INSERT INTO transactions(id, date, species, description, amount) VALUES (null, ${date}, ${species}, ${desc}, ${amt})""".update.apply() 
        true
      }
      case _ => false
    }
  }
}

