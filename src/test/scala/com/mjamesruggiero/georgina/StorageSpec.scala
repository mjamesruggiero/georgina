package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.fixture.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatra.test.scalatest._
import scala.util.{Success, Failure}

class StorageSpec extends ScalatraFlatSpec with BeforeAndAfter {

  import DB._

  lazy val config = new TestEnv

  lazy val format = DateTimeFormat.forPattern("yyyy-MM-dd");

  lazy val testDates: Map[String, org.joda.time.DateTime] = Map(
    "startDate" -> DateTime.parse("2013-12-13"),
    "endDate" -> DateTime.parse("2014-01-13")
  )

  before {
    val fixtures = List("DELETE FROM transactions",
    """INSERT into transactions
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

  "#store" should "store a transaction" in {
    val currentCountQuery = "SELECT COUNT(*) AS count FROM transactions"

    // TODO extract this into a test helper
    val countBefore = query(currentCountQuery, Map("count" -> mkInt), TestDatabase). map { row =>
      row.get("count").fold(0)(asInt)
    }.headOption.getOrElse(0)

    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Office Depot")
    Storage.store(t, TestDatabase)

    val countAfter = query(currentCountQuery, Map("count" -> mkInt), TestDatabase). map { row =>
      row.get("count").fold(0)(asInt)
    }.headOption.getOrElse(0)

    countAfter should equal(countBefore + 1)
  }

  it should "create a new record only if one does not exist" in {
    val t = new Transaction(1L, testDates("startDate"), "debit", -20.00, "unknown", "Github")
    val currentCountQuery =s"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date = '${t.date.toString(format)}'
          AND species = '${t.species}'
          AND description= '${t.description}'
          AND amount = ${t.amount}"""

    val countBefore = query(currentCountQuery, Map("count" -> mkInt), TestDatabase). map { row =>
      row.get("count").fold(0)(asInt)
    }.headOption.getOrElse(0)

    Storage.store(t, TestDatabase)

    val countAfter = query(currentCountQuery, Map("count" -> mkInt), TestDatabase). map { row =>
      row.get("count").fold(0)(asInt)
    }.headOption.getOrElse(0)

    countAfter should be > 0
    countAfter should equal(countBefore)
  }

  it should "store assets as assets" in {
    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "dividends", "fake bank transfer")
    val result = Storage.store(t, TestDatabase)

    val speciesQuery = """
          SELECT DISTINCT species
          FROM transactions WHERE description='fake bank transfer'
          AND amount=20.00"""

    val species = query(speciesQuery, Map("species" -> mkString), TestDatabase). map { row =>
      row.get("species").fold("")(asString)
    }.headOption match {
      case Some(s) => s
      case _ => ""
    }
    species should equal("asset")
  }

  "#inDateSpan" should "select transcations in a date range" in {
    val result = Storage.inDateSpan(testDates("startDate"),
                                    testDates("endDate"),
                                    TestDatabase)
    val description = result.head.description
    description should equal("test purchase 2")
  }

  "#withCategory" should "select category details" in {
    val category = "utilities"

    val result = Storage.withCategory(category,
                                      testDates("startDate"),
                                      testDates("endDate"),
                                      TestDatabase)

    val description = result.head.description
    description should equal("Github")
  }

  "#categoryStats" should "select category summary" in {
    val category = "utilities"

    val result = Storage.categoryStats(testDates("startDate"),
                                       testDates("endDate"),
                                       TestDatabase)
    val mean = result.last.mean
    mean should equal(-20.0)
  }

  "#byWeek" should "bin debit sums by calendar week" in {
    val result = Storage.byWeek(TestDatabase)
    val expected = List(
      DateSummary(DateTime.parse("2014-01-12"),-40.0,2),
      DateSummary(DateTime.parse("2013-12-08"),-40.0,2)
    )
    // crazy that they can't be compared directly; I blame MySql
    expected.toString should equal(result.toString)
  }
}
