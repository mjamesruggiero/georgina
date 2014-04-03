package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class GeorginaServletSpec extends ScalatraSuite with FunSuite {
  addServlet(classOf[GeorginaServlet], "/*")

  test("simple GET") {
    get("/") {
      status should equal (200)
      body should include ("Georgina")
    }
  }
}
