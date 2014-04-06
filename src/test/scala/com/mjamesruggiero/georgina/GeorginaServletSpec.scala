package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class GeorginaServletSpec extends ScalatraSuite with FunSuite {
  addServlet(classOf[GeorginaServlet], "/*")

  test("index should return 200") {
    get("/") {
      status should equal (200)
      body should include ("Georgina")
    }
  }
}
