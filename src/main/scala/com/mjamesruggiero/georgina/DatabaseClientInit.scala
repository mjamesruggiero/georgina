package com.mjamesruggiero.georgina

import scalikejdbc._
import org.slf4j.LoggerFactory

trait DatabaseClientInit {
  val logger = LoggerFactory.getLogger(getClass)

  def configureDatabaseClient() {
    val url = sys.env.getOrElse("GEORGINA_DATABASE_URL", "jdbc:mysql://localhost:3306/georgina_development")
    val user = sys.env.getOrElse("GEORGINA_DATABASE_USER", "root")
    val password = sys.env.getOrElse("GEORGINA_DATABASE_PASSWORD", "password")
    ConnectionPool.singleton(url, user, password)
  }

  def shutDownDatabaseClient() {
    logger.info("Shutting down database client")
  }
}

