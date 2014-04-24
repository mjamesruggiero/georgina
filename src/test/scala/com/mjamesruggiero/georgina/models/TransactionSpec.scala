package com.mjamesruggiero.georgina.models

import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._

class TransactionSpec extends ScalatraSuite with FunSuite {
  val tDate = DateTime.parse("2013-12-13")

  test("Transaction behaves like a transaction") {
    val t = Transaction(tDate, "debit", 100.00, "unknown", "Amazon.com")
    t.species should equal("debit")
    t.amount should equal(100.00)
    t.description should equal("Amazon.com")
    t.date should equal(tDate)
  }

  test("#averageAmount returns average amount") {
    val tSet = new TransactionSet(
      List(
        Transaction(tDate, "debit", 1.00, "unknown", "Amazon.com"),
        Transaction(tDate, "debit", 2.00, "unknown", "Amazon.com"),
        Transaction(tDate, "debit", 3.00, "unknown", "Amazon.com"),
        Transaction(tDate, "debit", 9.02, "unknown", "Amazon.com")
      )
    )
    tSet.averageAmount should equal(3.755)
  }

  val transactions = List(
      Transaction(tDate, "debit", 100.00,  "unknown", "Amazon.com"),
      Transaction(tDate, "debit", 200.00,  "unknown", "Amazon.com"),
      Transaction(tDate, "asset", 1000.00, "unknown", "Big Company"),
      Transaction(tDate, "asset", 1000.00, "unknown", "Big Company"),
      Transaction(tDate, "asset", 2000.00, "unknown", "Big Company")
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
        Transaction(DateTime.parse("2013-12-01"), "debit", 100.00, "unknown", "Amazon.com"),
        Transaction(january, "debit", 200.00, "unknown", "Amazon.com"),
        Transaction(january, "asset", 1000.00,"unknown", "Big Company")
    )
    val tSet = new TransactionSet(datedTrans)
    tSet.byDate(january).map(_.amount).sum should equal(1200.0)
  }

}
