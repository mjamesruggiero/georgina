package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.scalatest.fixture.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback

class StorageSpec extends FlatSpec with AutoRollback with ShouldMatchers with BeforeAndAfter {

  lazy val config = new TestEnv

  lazy val testDates: Map[String, org.joda.time.DateTime] = Map(
    "startDate" -> DateTime.parse("2013-12-13"),
    "endDate" -> DateTime.parse("2014-01-13")
  )

  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()
  }

  override def fixture(implicit session: DBSession) {
    sql"insert into transactions values (NULL, ${DateTime.now}, 'debit', 'Github', 'utilities', 20.00)".update.apply()

    // for the datespan
    val january13 = DateTime.parse("2014-01-13")
    sql"INSERT into Transactions VALUES (NULL, ${january13}, 'debit', 'January purchase', 'personal', 20.00)".update.apply()
  }

  it should "store a new transaction" in { implicit session =>
    val returned: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      .map(rs => rs.int("count"))
      .single.apply()
    val countBefore = returned match {
      case Some(num) => num
      case _ => 0
    }

    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Office Depot")
    Storage.store(config.env, t)
    val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      .map(rs => rs.int("count"))
      .single.apply()
    val countAfter = postHoc match {
      case Some(num) => num
      case _ => 0
    }
    countAfter should equal(countBefore + 1)
  }

  it should "create a new record only if one does not exist" in { implicit session =>
    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Github")
    val returned: Option[Int] = sql"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date=${t.date}
          AND species=${t.species}
          AND description=${t.description}
          AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    val countBefore = returned match {
      case Some(num) => num
      case _ => 0
    }
    Storage.store(config.env, t)

    val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date=${t.date}
          AND species=${t.species}
          AND description=${t.description}
          AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    val countAfter = postHoc match {
      case Some(num) => num
      case _ => 0
    }
    countAfter should equal(countBefore)
  }

  it should "store an asset as an asset" in { implicit session =>
    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "dividends", "Bank")
    val result = Storage.store(config.env, t)

    val postHoc: Option[String] = sql"""SELECT DISTINCT species
          FROM transactions
          WHERE description='Bank'
          AND amount=20.00""".map(rs => rs.string("species")).single.apply()

    val species = postHoc match {
      case Some(s) => s
      case _ => "nope"
    }
    species should equal("asset")
  }

  it should "let you select transcations in a date range" in { implicit session =>
    val result = Storage.inDateSpan(config.env,
                                    testDates("startDate"),
                                    testDates("endDate"))
    val description = result.head.description
    description should equal("January purchase")
  }

  it should "let you select category details" in { implicit session =>
    val category = "utilities"

    val result = Storage.withCategory(config.env,
                                      category,
                                      testDates("startDate"),
                                      testDates("endDate"))
    val description = result.head.description
    description should equal("Github")
  }

  it should "let you select category summary" in { implicit session =>
    val category = "utilities"

    val result = Storage.categoryStats(config.env,
                                       testDates("startDate"),
                                       testDates("endDate"))
    val description = result.head.mean
    description should equal(20.0)
  }
}
