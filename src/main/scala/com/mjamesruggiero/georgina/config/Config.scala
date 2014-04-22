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
