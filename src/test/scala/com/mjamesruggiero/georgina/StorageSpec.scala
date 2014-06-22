package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.BeforeAndAfter
import org.scalatest.fixture.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback
import scala.util.{Success, Failure}

class StorageSpec extends FlatSpec with AutoRollback with ShouldMatchers with BeforeAndAfter {

  lazy val config = new TestEnv

  lazy val format = DateTimeFormat.forPattern("yyyy-MM-dd");

  lazy val testDates: Map[String, org.joda.time.DateTime] = Map(
    "startDate" -> DateTime.parse("2013-12-13"),
    "endDate" -> DateTime.parse("2014-01-13")
  )



  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()

    val fixtures = List("DELETE FROM transactions",
    """INSERT into Transactions
          VALUES (NULL,
                  '2013-12-13',
                  'debit',
                  'test purchase 1',
                  'personal',
                  -20.00)""",
    """INSERT into Transactions
          VALUES (NULL,
                  '2014-01-13',
                  'debit',
                  'test purchase 2',
                  'personal',
                  -20.00)""",
    """INSERT INTO transactions
          VALUES (NULL,
                  '2013-12-13',
                  'debit',
                  'Github',
                  'utilities',
                  -20.00)""",
    """INSERT into Transactions
          VALUES (NULL,
                  '2014-01-13',
                  'debit',
                  'test purchase 3',
                  'personal',
                  -20.00)""")

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

  //override def fixture(implicit session: DBSession) {
    //sql"""DELETE FROM transactions""".update.apply()
    //sql"""INSERT INTO transactions
          //VALUES (NULL,
                  //${testDates("startDate")},
                  //'debit',
                  //'Github',
                  //'utilities',
                  //-20.00)""".update.apply()

    //// for the datespan
    //val january13 = DateTime.parse("2014-01-13")
    //sql"""INSERT into Transactions
          //VALUES (NULL,
                  //${january13},
                  //'debit',
                  //'January purchase',
                  //'personal',
                  //-20.00)""".update.apply()
  //}

  //"#store" should "store a transaction" in { implicit session =>
    //val returned: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      //.map(rs => rs.int("count"))
      //.single.apply()
    //val countBefore = returned match {
      //case Some(num) => num
      //case _ => 0
    //}

    //val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Office Depot")
    //Storage.store(config.env, t)
    //val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      //.map(rs => rs.int("count"))
      //.single.apply()
    //val countAfter = postHoc match {
      //case Some(num) => num
      //case _ => 0
    //}
    //countAfter should equal(countBefore + 1)
  //}

  //it should "create a new record only if one does not exist" in { implicit session =>
    //val t = new Transaction(1L, testDates("startDate"), "debit", -20.00, "unknown", "Github")
    //val returned: Option[Int] = sql"""SELECT COUNT(*) AS count
          //FROM transactions
          //WHERE date=${t.date}
          //AND species=${t.species}
          //AND description=${t.description}
          //AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    //val countBefore = returned match {
      //case Some(num) => num
      //case _ => 0
    //}
    //Storage.store(config.env, t)

    //val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count
          //FROM transactions
          //WHERE date=${t.date}
          //AND species=${t.species}
          //AND description=${t.description}
          //AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    //val countAfter = postHoc match {
      //case Some(num) => num
      //case _ => 0
    //}
    //countAfter should be > 0
    //countAfter should equal(countBefore)
  //}

  //it should "store assets as assets" in { implicit session =>
    //val t = new Transaction(1L, DateTime.now, "debit", 20.00, "dividends", "Bank")
    //val result = Storage.store(config.env, t)

    //val postHoc: Option[String] = sql"""SELECT DISTINCT species
          //FROM transactions
          //WHERE description='Bank'
          //AND amount=20.00""".map(rs => rs.string("species")).single.apply()

    //val species = postHoc match {
      //case Some(s) => s
      //case _ => "nope"
    //}
    //species should equal("asset")
  //}

  //"inDateSpan" should "select transcations in a date range" in { implicit session =>
    //val result = Storage.inDateSpan(config.env,
                                    //testDates("startDate"),
                                    //testDates("endDate"))
    //val description = result.head.description
    //description should equal("January purchase")
  //}

  //"#withCategory" should "select category details" in { implicit session =>
    //val category = "utilities"

    //val result = Storage.withCategory(config.env,
                                      //category,
                                      //testDates("startDate"),
                                      //testDates("endDate"))
    //val description = result.head.description
    //description should equal("Github")
  //}

  "#categoryStats" should "select category summary" in { implicit session =>
    val category = "utilities"

    val result = Storage.categoryStats(testDates("startDate"),
                                       testDates("endDate"),
                                       TestDatabase)
    val mean = result.last.mean
    mean should equal(-20.0)
  }

  "#byWeek" should "bin debit sums by calendar week" in { implicit session =>
    val result = Storage.byWeek(TestDatabase)
    val expected = List(
      DateSummary(DateTime.parse("2014-01-12"),-40.0,2),
      DateSummary(DateTime.parse("2013-12-08"),-40.0,2)
    )
    // crazy that they can't be compared directly; I blame MySql
    expected.toString should equal(result.toString)
  }
}
