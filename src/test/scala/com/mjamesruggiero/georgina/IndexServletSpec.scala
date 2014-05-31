package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._

class IndexServletSpec extends ScalatraFlatSpec {
  addServlet(new IndexServlet("test"), "/*")

  "GET /" should "return 200" in  {
    get("/") {
      status should equal (200)
    }
  }
}
