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

class CategoryServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
  lazy val config = new TestEnv
  lazy val earlierDate = DateTime.parse("2014-01-01")
  lazy val laterDate = DateTime.parse("2014-02-01")

  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()
    buildFixture
  }

  after {
    removeFixture
  }

  def buildFixture(implicit session: DBSession = AutoSession) {
    sql"insert into transactions values (NULL, ${earlierDate}, 'debit', 'CVS', 'medical', -20.00)".update.apply()
    sql"insert into transactions values (NULL, ${laterDate}, 'debit', 'Trader Joe', 'grocery', -20.00)".update.apply()
  }

  def removeFixture(implicit session: DBSession = AutoSession) {
    sql"""DELETE FROM transactions
          WHERE (description='Trader Joe' OR description='CVS')""".update.apply()
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
