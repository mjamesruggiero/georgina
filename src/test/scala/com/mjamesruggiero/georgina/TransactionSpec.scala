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
    val trans = List(
      Transaction("2013-12-13", "debit", 1.00, "Amazon.com"),
      Transaction("2013-12-13", "debit", 2.00, "Amazon.com"),
      Transaction("2013-12-13", "debit", 3.00, "Amazon.com"),
      Transaction("2013-12-13", "debit", 9.02, "Amazon.com")
    )
    val tSet = new TransactionSet(trans)
    tSet.averageAmount should equal(3.755)
  }
}
