package com.mjamesruggiero.georgina.controllers

import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._
import scala.util.{Success, Failure}

class TransactionControllerSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv
  lazy val earlierDate = DateTime.parse("2014-01-01")
  lazy val laterDate = DateTime.parse("2014-02-01")

  val fixtures = List(
    s"insert into transactions values (NULL, '${Utils.canonicalDate(earlierDate)}', 'debit', 'Github', 'personal', 20.00)",
    s"insert into transactions values (NULL, '${Utils.canonicalDate(laterDate)}', 'debit', 'Wells Fargo', 'bank', 20.00)"
  )

  before {
    for(q : String <- fixtures) {
      DB.update(q, TestDatabase) match {
        case Success(_) => None
        case Failure(ex) => fail(s"database setup error: ${ex.getMessage}")
      }
    }
  }

  after {
    DB.update("DELETE FROM transactions", TestDatabase) match {
      case Success(_) => None
      case Failure(ex) => fail(s"database setup error: ${ex.getMessage}")
    }
  }

  addServlet(new TransactionController(TestDatabase), "/*")

  "GET /" should "return 200" in  {
    get("/") {
      status should equal (200)
    }
  }

  "POST /submit" should "fail with bad JSON" in {
    val input = """{"foo":[ {"bar":"baz" }]}"""
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
      response.body should include("unable to parse JSON")
    }
  }

  it should "succeed with good JSON" in {
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
          "description":"7-11"
        }
      ]
    }
    """
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(201)
    }
  }

  it should "fail with bad record" in {
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

  "GET /transactions/<date-params>" should "retrieve transactions in a range" in {
    get("/?start=2014-01-15&end=2014-02-05") {
      status should equal (200)
      body should include ("Wells Fargo")
      body should not include ("Github")
    }
  }

  "GET /transactions/all<date-params>" should "handle bad date params" in {
    get("/?start=2014-01-15&end=fasi82-0x05") {
      status should equal (500)
      body should include ("error: invalid params")
    }
  }

  "POST /" should "fail with bad JSON" in {
    val input = """{"foo":[ {"bar":"baz" }]}"""
    post("/", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
      response.body should include("unable to parse JSON")
      response.body should include("unable to parse JSON")
    }
  }

  "POST /" should "succeed with good JSON" in {
    val input = """
        {
          "id": 0,
          "date": "2014-05-16",
          "amount": -99.99,
          "category": "grocery",
          "description": "Whole Foods"
        }"""
    post("/", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal (200)
    }
    val earlierDate = DateTime.parse("2014-05-01")
    val laterDate = DateTime.parse("2014-05-17")
    val inDb = Storage.withCategory("grocery", earlierDate, laterDate, TestDatabase)
    inDb.length should be(1)
  }

  "PUT /:id" should "update the record" in {
    val earlierDate = DateTime.parse("2014-01-01")
    val existingInRange = Storage.inDateSpan(earlierDate, earlierDate, TestDatabase);
    val id = existingInRange.head.id.toInt

    val input = s"""
        {
          "id": ${id},
          "date": "2014-01-01",
          "amount": -19.99,
          "category": "grocery",
          "description": "Whole Foods"
        }"""

    put(s"/${id}", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal (200)
    }
    val persisted = Storage.getById(id, TestDatabase)
    val persistedAmount = persisted match {
        case Some(t) => t.amount
        case _ => 0
    }
    persistedAmount should equal(-19.99)
  }

  "GET /:id" should "return a single record" in {
    val earlierDate = DateTime.parse("2014-01-01")
    val existingInRange = Storage.inDateSpan(earlierDate, earlierDate, TestDatabase);
    val id = existingInRange.head.id.toInt
    val persisted = Storage.getById(id, TestDatabase)

    get(s"/${id}") {
      status should equal (200)
      response.body should include("Github")
    }
  }
}
