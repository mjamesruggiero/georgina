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

class GeorginaServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv

  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()
    buildFixture
  }

  after {
    removeFixture
  }

  /*
  * TODO put these in a test helper
  **/
  def buildFixture(implicit session: DBSession = AutoSession) {
    sql"insert into transactions values (NULL, ${DateTime.now}, 'debit', 'Github', 'personal', 20.00)".update.apply()
    sql"insert into transactions values (NULL, ${DateTime.now}, 'debit', 'Wells Fargo', 'bank', 20.00)".update.apply()
  }

  def removeFixture(implicit session: DBSession = AutoSession) {
    sql"DELETE FROM transactions WHERE species='debit' AND description='Github'".update.apply()
  }

  addServlet(new GeorginaServlet("test"), "/*")

  "GET /" should "return 200" in  {
    get("/") {
      status should equal (200)
      body should include ("Georgina")
    }
  }

  "POST /submit" should "fail with bad JSON" in {
    val input = """{"foo":[ {"bar":"baz" }]}"""
    post("submit", input.getBytes("UTF-8"), Map("Content-Type" -> "application/json")) {
      status should equal(500)
      response.body should endWith ("unable to parse JSON")
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
          "description":"McDonald's"
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

  "GET /categories/:category" should "retrieve transactions with a category" in {
    get("/categories/personal") {
      status should equal (200)
      body should include ("Github")
      body should not include ("Wells Fargo")
    }
  }
}
