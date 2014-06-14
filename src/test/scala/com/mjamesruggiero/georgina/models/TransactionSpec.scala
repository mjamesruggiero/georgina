package com.mjamesruggiero.georgina.models

import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._

class TransactionSpec extends ScalatraSuite with FunSuite {
  val tDate = DateTime.parse("2013-12-13")
  val format = DateTimeFormat.forPattern("yyyy-MM-dd");

  test("Transaction behaves like a transaction") {
    val t = Transaction(1L, tDate, "debit", 100.00, "unknown", "Amazon.com")
    t.species should equal("debit")
    t.amount should equal(100.00)
    t.description should equal("Amazon.com")
    t.date should equal(tDate)
  }

  test("#averageAmount returns average amount") {
    val tSet = new TransactionSet(
      List(
        Transaction(1L, tDate, "debit", 1.00, "unknown", "Amazon.com"),
        Transaction(1L, tDate, "debit", 2.00, "unknown", "Amazon.com"),
        Transaction(1L, tDate, "debit", 3.00, "unknown", "Amazon.com"),
        Transaction(1L, tDate, "debit", 9.02, "unknown", "Amazon.com")
      )
    )
    tSet.averageAmount should equal(3.755)
  }

  val transactions = List(
      Transaction(1L, tDate, "debit", 100.00,  "unknown", "Amazon.com"),
      Transaction(1L, tDate, "debit", 200.00,  "unknown", "Amazon.com"),
      Transaction(1L, tDate, "asset", 1000.00, "unknown", "Big Company"),
      Transaction(1L, tDate, "asset", 1000.00, "unknown", "Big Company"),
      Transaction(1L, tDate, "asset", 2000.00, "unknown", "Big Company")
  )

  test("#amountBySpecies returns amount by species") {
    val tSet = new TransactionSet(transactions)
    tSet.amountBySpecies("debit") should equal(300.00)
    tSet.amountBySpecies("asset") should equal(4000.00)
  }

  test("#averageSpend gets average spend") {
    val tSet = new TransactionSet(transactions)
    tSet.averageSpend should equal(150.00)
  }

  test("#standardDeviation returns standard deviation") {
    val tSet = new TransactionSet(transactions)
    tSet.standardDeviation("debit") should equal(50.0)
  }

  test("#withDescription filters by description") {
    val tSet = new TransactionSet(transactions)
    val filtered = tSet.withDescription("Amazon.com")
    filtered.map(_.amount).sum should equal(300.00)
  }

  test("#debits returns only debits") {
    val tSet = new TransactionSet(transactions)
    tSet.debits.map(_.amount) should equal(List(100., 200.0))
  }

  test("#byDate groups by date") {
    val january = DateTime.parse("2014-01-01")
    val datedTrans = List(
        Transaction(1L, DateTime.parse("2013-12-01"), "debit", 100.00, "unknown", "Amazon.com"),
        Transaction(1L, january, "debit", 200.00, "unknown", "Amazon.com"),
        Transaction(1L, january, "asset", 1000.00,"unknown", "Big Company")
    )
    val tSet = new TransactionSet(datedTrans)
    tSet.byDate(january).map(_.amount).sum should equal(1200.0)
  }

  test("#timeSeriesSums groups by date, offers a sum each day's debits") {
    val january = DateTime.parse("2014-01-01")
    val datedTrans = List(
        Transaction(1L, DateTime.parse("2013-12-01"), "debit", -100.00, "unknown", "Amazon.com"),
        Transaction(1L, january, "debit", -200.00, "unknown", "Amazon.com"),
        Transaction(1L, january, "asset", 1000.00,"unknown", "Big Company")
    )
    val tSet = new TransactionSet(datedTrans)
    tSet.timeSeriesSums(0) match {
      case(date, sum) => sum should equal(100.0)
    }
  }

  test("#timeSeriesSumsWithDefaultZeros assigns zero for any day with no debits") {
    val missingDayBetween = List(
        Transaction(1L, DateTime.parse("2013-12-30"), "debit", -100.00, "unknown", "Amazon.com"),
        Transaction(1L, DateTime.parse("2014-01-01"), "debit", -200.00, "unknown", "Amazon.com")
    )
    val result = new TransactionSet(missingDayBetween).timeSeriesSumsWithDefaultZeros
    result(1) should be ("2013-12-31", 0.0)
  }
}
