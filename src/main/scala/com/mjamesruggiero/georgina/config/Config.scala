package com.mjamesruggiero.georgina.config

trait Config {
  val env: String
}

class DevelopmentEnv extends Config {
  val env = "development"
}

class ProductionConfig extends Config {
  val env = "production"
}
