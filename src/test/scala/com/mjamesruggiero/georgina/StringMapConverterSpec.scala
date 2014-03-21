package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import org.joda.time.DateTime

class StringMapConverterSpec extends ScalatraSuite with FunSuite {
  val testMap = Map("date" -> "03/29/2013",
                    "amount" -> "100.00",
                    "description" -> "FAKE DESCRIPTION",
                    "species" -> "debit")
  val converted = new StringMapConverter(testMap).convert

  test("#convert can build a date") {
    converted.date should equal(DateTime.parse("2013-03-29"))
  }

  test("#convert can build a species") {
    converted.species should equal("debit")
  }

  test("#convert can build an amount") {
    converted.amount should equal(100.00)
  }

  test("#convert can build a description") {
    converted.description should equal("FAKE DESCRIPTION")
  }
}
