package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.models._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import org.joda.time.DateTime

class UtilsSpec extends ScalatraSuite with FunSuite {
  val tDate = DateTime.parse("2013-12-13")

  test("#transactionHash returns a hash for a transaction") {
    val t = Transaction(1L, tDate, "debit", 100.00, "unknown", "Amazon.com")
    val testHash = Utils.transactionHash(t)
    val expected = "d49e9b72c8beb451a2845ebfcf35edf098067931f27df7356cb851aeb7caea5b"
    testHash should equal(expected)
  }

  test("getDatesBetween gets all the dates between two dates") {
    val startDate = DateTime.parse("2013-12-13")
    val endDate = DateTime.parse("2013-12-15")
    val returned = Utils.getDatesBetween(startDate, endDate)
    val expected = List(DateTime.parse("2013-12-13"), DateTime.parse("2013-12-14"),DateTime.parse("2013-12-15") )
    returned should equal(expected)
  }
}
