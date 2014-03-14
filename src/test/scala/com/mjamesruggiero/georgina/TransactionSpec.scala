package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.Transaction._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class TransactionSpec extends ScalatraSuite with FunSuite {
  test("transaction behaves like a transaction") {
    val t = Transaction("2013-12-13", "debit", 100.00, "Amazon.com")
    t.species should equal("debit")
    t.amount should equal(100.00)
    t.description should equal("Amazon.com")
    t.date should equal("2013-12-13")
  }

  test("Transaction set can get average amount") {
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

  val largeTransSet = List(
      Transaction("2013-12-13", "debit", 100.00, "Amazon.com"),
      Transaction("2013-12-13", "debit", 200.00, "Amazon.com"),
      Transaction("2013-12-13", "asset", 1000.00, "Big Company"),
      Transaction("2013-12-13", "asset", 1000.00, "Big Company"),
      Transaction("2013-12-13", "asset", 2000.00, "Big Company")
  )

  test("Transaction set can get amount by species") {
    val tSet = new TransactionSet(largeTransSet)
    tSet.amountBySpecies("debit") should equal(300.00)
    tSet.amountBySpecies("asset") should equal(4000.00)
  }

  test("Transaction set can get average spend") {
    val tSet = new TransactionSet(largeTransSet)
    tSet.averageSpend should equal(150.00)
  }

  test("Transaction set can get standard deviation") {
    val tSet = new TransactionSet(largeTransSet)
    tSet.standardDeviation("debit") should equal(50.0)
  }
}
