package com.mjamesruggiero.georgina.controllers

import org.scalatra.test.scalatest._
import com.mjamesruggiero.georgina.config._

class IndexControllerSpec extends ScalatraFlatSpec {
  addServlet(new IndexController(TestDatabase), "/*")

  "GET /" should "return 200" in  {
    get("/") {
      status should equal (200)
    }
  }
}
