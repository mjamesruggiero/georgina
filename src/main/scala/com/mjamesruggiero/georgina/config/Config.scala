package com.mjamesruggiero.georgina.config

trait Config {
  val env: String
}

class DevelopmentEnv extends Config {
  val env = "development"
}

class TestEnv extends Config {
  val env = "test"
}

class ProductionConfig extends Config {
  val env = "production"
}

trait DBConfig {
  val address: String
  val username: String
  val password: String
  val driverClassName: String
}

object TestDatabase extends DBConfig {
  val address = "jdbc:mysql://localhost:3306/georgina_test"
  val username = "root"
  val password = "password"
  val driverClassName = "com.mysql.jdbc.Driver"
}

object DevelopmentDatabase extends DBConfig {
  val address = "jdbc:mysql://localhost:3306/georgina_development"
  val username = "root"
  val password = "password"
  val driverClassName = "com.mysql.jdbc.Driver"
}
