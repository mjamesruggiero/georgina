package com.mjamesruggiero.georgina

import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback
import com.mjamesruggiero.georgina.config._
import org.joda.time.DateTime

class TransactionServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv

  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()
    buildFixture
  }

  after {
    removeFixture
  }

  def buildFixture(implicit session: DBSession = AutoSession) {
    val earlierDate = DateTime.parse("2014-01-01")
    val laterDate = DateTime.parse("2014-02-01")
    sql"insert into transactions values (NULL, ${earlierDate}, 'debit', 'Github', 'personal', 20.00)".update.apply()
    sql"insert into transactions values (NULL, ${laterDate}, 'debit', 'Wells Fargo', 'bank', 20.00)".update.apply()
  }

  def removeFixture(implicit session: DBSession = AutoSession) {
    sql"""DELETE FROM transactions
          WHERE (description='Whole Foods' OR
               description='7-11' OR
               description='PG & E' OR
               description='Github' OR
               description='Wells Fargo')""".update.apply()
  }

  addServlet(new TransactionServlet("test"), "/*")

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
      status should equal(200)
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
    val inDb = Storage.withCategory(config.env, "grocery", earlierDate, laterDate)
    inDb.length should be(1)
  }

  "PUT /:id" should "update the record" in {
    val earlierDate = DateTime.parse("2014-01-01")
    val existingInRange = Storage.inDateSpan(config.env,
                                    earlierDate, earlierDate);
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
    val persisted = Storage.getById(config.env, id)
    val persistedAmount = persisted match {
        case Some(t) => t.amount
        case _ => 0
    }
    persistedAmount should equal(-19.99)
  }

  "GET /:id" should "return a single record" in {
    val earlierDate = DateTime.parse("2014-01-01")
    val existingInRange = Storage.inDateSpan(config.env, earlierDate, earlierDate);
    val id = existingInRange.head.id.toInt
    val persisted = Storage.getById(config.env, id)

    get(s"/${id}") {
      status should equal (200)
      response.body should include("Github")
    }
  }
}
