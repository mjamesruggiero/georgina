package com.mjamesruggiero.georgina.controllers

import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import org.scalatra._
import scalate.ScalateSupport

class TestController(config: DBConfig)  extends GeorginaStack with ScalateSupport {

  get("/") {
    contentType="text/html"
    ssp("/georgina/test")
  }
}
