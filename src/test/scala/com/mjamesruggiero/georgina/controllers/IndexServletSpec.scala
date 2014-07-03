package com.mjamesruggiero.georgina.controllers

import org.scalatra.test.scalatest._
import com.mjamesruggiero.georgina.config._

class IndexServletSpec extends ScalatraFlatSpec {
  addServlet(new IndexServlet(TestDatabase), "/*")

  "GET /" should "return 200" in  {
    get("/") {
      status should equal (200)
    }
  }
}
