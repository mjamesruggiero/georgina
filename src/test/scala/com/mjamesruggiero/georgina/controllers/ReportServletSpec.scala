package com.mjamesruggiero.georgina.controllers

import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import org.joda.time.DateTime
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._
import scala.util.{Success, Failure}

class ReportServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv
  lazy val earlierDate = DateTime.parse("2014-05-01")
  lazy val laterDate = DateTime.parse("2014-05-02")

  val fixtures = List(
    s"INSERT INTO transactions VALUES (NULL, '${Utils.canonicalDate(earlierDate)}', 'debit', 'Github', 'personal', -20.00)",
    s"INSERT INTO transactions VALUES (NULL, '${Utils.canonicalDate(laterDate)}', 'debit', 'Wells Fargo', 'bank', -20.00)"
  )

  val deletes = List(
    "DELETE FROM transactions"
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
    for(q : String <- deletes) {
      DB.update(q, TestDatabase) match {
        case Success(_) => None
        case Failure(ex) => fail(s"database setup error: ${ex.getMessage}")
      }
    }
  }

  addServlet(new ReportServlet(TestDatabase), "/*")

  "GET /byday" should "return 200" in  {
    get("/byday") {
      status should equal (200)
    }
  }

  "GET /byday" should "return debits summed, grouped by day" in  {
    get("/byday?start=2014-05-01&end=2014-05-02") {
      status should equal (200)
      response.body should include("2014-05-01")
      response.body should include("2014-05-02")
    }
  }

  "GET /byweek" should "return debits summed, grouped by week" in  {
    get("/byweek?start=2014-05-01&end=2014-05-30") {
      status should equal (200)
      response.body should include("2014-04-27") // the monday of that week
      response.body should include("-40")
    }
  }
}
