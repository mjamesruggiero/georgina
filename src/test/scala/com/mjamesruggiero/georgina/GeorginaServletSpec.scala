package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class GeorginaServletSpec extends ScalatraFlatSpec {

  addServlet(new GeorginaServlet("test"), "/*")

  it should "return 200" in  {
    get("/") {
      status should equal (200)
      body should include ("Georgina")
    }
  }

  it should "POST /submit fails with bad JSON" in {
    val input = """{"foo":[ {"bar":"baz" }]}"""
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
      response.body should endWith ("unable to parse JSON")
    }
  }

  it should "POST /submit succeeds with good JSON" in {
    val input = """
    {
      "transactions":
      [
        {
          "id": 0,
          "date":"2014-04-07",
          "amount": 99.99,
          "category": "unknown",
          "description":"PG & E"
        },
        {
          "id": 0,
          "date":"2014-04-04",
          "category": "unknown",
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

  it should "POST /submit fails with bad record" in {
    val input = """
    {
      "transactions":
      [
        {
          "date":"2014-04-07",
          "category": "unknown",
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
