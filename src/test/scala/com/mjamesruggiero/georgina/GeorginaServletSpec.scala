package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class GeorginaServletSpec extends ScalatraSuite with FunSuite {
  addServlet(new GeorginaServlet("test"), "/*")

  test("index should return 200") {
    get("/") {
      status should equal (200)
      body should include ("Georgina")
    }
  }

  test("POST /submit fails with bad JSON") {
    val input = """{"foo":[ {"bar":"baz" }]}"""
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
      response.body should endWith ("unable to parse JSON")
    }
  }

  test("POST /submit succeeds with good JSON") {
    val input = """
    {
      "transactions":
      [
        {
          "date":"2014-04-07",
          "amount": 99.99,
          "description":"PG & E"
        },
        {
          "date":"2014-04-04",
          "amount": 20.00,
          "description":"McDonald's"
        }
      ]
    }
    """
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(200)
    }
  }

  test("POST /submit fails with bad record") {
    val input = """
    {
      "transactions":
      [
        {
          "date":"2014-04-07",
          "amount": "BAD RECORD",
          "description":"PG & E"
        }
      ]
    }
    """
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
    }
  }
}
