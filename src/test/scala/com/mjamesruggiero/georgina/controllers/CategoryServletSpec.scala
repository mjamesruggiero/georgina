package com.mjamesruggiero.georgina.controllers

import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import org.joda.time.DateTime
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._
import scala.util.{Success, Failure}

class CategoryServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv
  lazy val earlierDate = DateTime.parse("2014-01-01")
  lazy val laterDate = DateTime.parse("2014-02-01")

  val fixtures = List(
    s"INSERT INTO transactions VALUES (NULL, '${Utils.canonicalDate(earlierDate)}', 'debit', 'CVS', 'medical', -20.00)",
    s"INSERT INTO transactions VALUES (NULL, '${Utils.canonicalDate(laterDate)}', 'debit', 'Trader Joe', 'grocery', -20.00)"
  )

  val deletes = List(
    "DELETE FROM transactions"
  )

  before {
    // TODO extract this to a test helper
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

  addServlet(new CategoryServlet(TestDatabase), "/categories/*")

  "GET /categories/:category" should "retrieve transactions with a category" in {
    get("/categories/medical?start=2014-01-01&end=2014-02-01") {
      status should equal (200)
      body should include ("CVS")
      body should not include ("Wells Fargo")
    }
  }

  "GET /categories/:category" should "handle bad date params" in {
    get("/categories/personal?start=asd72-15&end=2014-04-01") {
      status should equal (500)
      body should include ("""{"name":"param error","message":"improper start and end dates"}""")
    }
  }

  "GET /categories" should "retrieve summary index for categories" in {
    get("/categories?start=2014-01-15&end=2014-02-05") {
      status should equal (200)
      body should include ("""{"category":"grocery","count":1,"mean":-20,"standard_deviation":0}""")
    }
  }

  "GET /categories" should "handle bad date params" in {
    get("/categories?start=2014-01-15&end=foo") {
      status should equal (500)
      body should include ("error")
    }
  }
}
