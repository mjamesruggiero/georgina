package com.mjamesruggiero.georgina

import scalikejdbc._
import SQLInterpolation._

object Storage {

  def init: List[Map[String, Any]] = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
    implicit val session = AutoSession

    sql"""CREATE TABLE members(id serial NOT NULL PRIMARY KEY, name VARCHAR(64), created_at TIMESTAMP NOT NULL)""".
      execute.apply()

    Seq("Alice", "Ted", "Bob") foreach { name =>
      sql"INSERT INTO members(name, created_at) VALUES (${name}, current_timestamp)".update.apply() 
    }

    val entities: List[Map[String, Any]] = sql"SELECT * FROM members".map(_.toMap).list.apply()
    entities
  }
}

