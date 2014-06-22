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

class ReportServletSpec extends ScalatraFlatSpec with BeforeAndAfter {
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
    val earlierDate = DateTime.parse("2014-05-01")
    val laterDate = DateTime.parse("2014-05-02")
    sql"insert into transactions values (NULL, ${earlierDate}, 'debit', 'Github', 'personal', -20.00)".update.apply()
    sql"insert into transactions values (NULL, ${laterDate}, 'debit', 'Wells Fargo', 'bank', -20.00)".update.apply()
  }

  def removeFixture(implicit session: DBSession = AutoSession) {
    sql"""DELETE FROM transactions
          WHERE (description='Github' OR
               description='Wells Fargo')""".update.apply()
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
}
