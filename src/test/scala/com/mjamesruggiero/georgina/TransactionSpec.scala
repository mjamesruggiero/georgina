package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.Transaction._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class TransactionSpec extends ScalatraSuite with FunSuite {
  test("Transaction behaves like a transaction") {
    val t = Transaction("2013-12-13", "debit", 100.00, "Amazon.com")
    t.species should equal("debit")
    t.amount should equal(100.00)
    t.description should equal("Amazon.com")
    t.date should equal("2013-12-13")
  }

  test("#averageAmount returns average amount") {
    val tSet = new TransactionSet(
      List(
        Transaction("2013-12-13", "debit", 1.00, "Amazon.com"),
        Transaction("2013-12-13", "debit", 2.00, "Amazon.com"),
        Transaction("2013-12-13", "debit", 3.00, "Amazon.com"),
        Transaction("2013-12-13", "debit", 9.02, "Amazon.com")
      )
    )
    tSet.averageAmount should equal(3.755)
  }

  val transactions = List(
      Transaction("2013-12-13", "debit", 100.00, "Amazon.com"),
      Transaction("2013-12-13", "debit", 200.00, "Amazon.com"),
      Transaction("2013-12-13", "asset", 1000.00, "Big Company"),
      Transaction("2013-12-13", "asset", 1000.00, "Big Company"),
      Transaction("2013-12-13", "asset", 2000.00, "Big Company")
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
}
