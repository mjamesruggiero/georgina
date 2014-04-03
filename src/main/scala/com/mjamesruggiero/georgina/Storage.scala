package com.mjamesruggiero.georgina

import scalikejdbc._
import SQLInterpolation._

object Storage {

  def init: List[Map[String, Any]] = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
    implicit val session = AutoSession

    sql"""CREATE TABLE users(id serial NOT NULL PRIMARY KEY, 
                               email VARCHAR(64), 
                               encrypted_password VARCHAR(64), 
                               first_name VARCHAR(64), 
                               last_name VARCHAR(64))""".execute.apply()

    val testUsers = Seq(("passwORD", "alice@example.com", "Alice", "Jones"), 
                        ("p@ssw4rd", "ted@example.com",   "Ted",   "Smith"), 
                        ("P@ZZwerD", "bob@example.com",   "Bob",   "Huang")) 
    
    testUsers foreach { xs =>
      xs match {
        case (encrypted_password, email, first_name, last_name) => sql"""INSERT INTO users(encrypted_password, email, first_name, last_name) 
                                                              VALUES (${encrypted_password}, ${email}, ${first_name}, ${last_name})""".update.apply() 
      }
    }

    val entities: List[Map[String, Any]] = sql"SELECT * FROM users".map(_.toMap).list.apply()
    entities
  }

  def dropTables: Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
    implicit val session = AutoSession

    sql"""DROP TABLE IF EXISTS users""".execute.apply()
  }
}

