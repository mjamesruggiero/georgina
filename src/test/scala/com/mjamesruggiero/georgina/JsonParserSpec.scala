package com.mjamesruggiero.georgina

import scalaz._, Scalaz._
import argonaut._, Argonaut._
import org.scalatest.FunSuite
import org.scalatra.test.scalatest._

class JsonParsersSpec extends ScalatraSuite with FunSuite {

  import com.mjamesruggiero.georgina.JSONParsers._

  test("JSON should parse correctly") {
    val testJson = """
        {
          "date":"2014-04-04",
          "amount": 20.00,
          "category": "unknown",
          "description":"Amazon.com"
        }
    """
    val returned = testJson.decodeOption[Line] match {
      case Some(line) => Some(line)
      case _ => None
    }
    returned.get.date should equal("2014-04-04")
    returned.get.description should equal("Amazon.com")
    returned.get.amount should equal(Some(20.00))
  }

  test("list of transactions JSON should parse correctly") {
    val testJson = """
    {
      "transactions":
      [
        {
          "date":"2014-04-07",
          "amount": 99.99,
          "category": "unknown",
          "description":"Target"
        },
        {
          "date":"2014-04-04",
          "category": "unknown",
          "amount": 20.00,
          "description":"Amazon.com"
        }
      ]
    }
    """
    val optionJson = Parse.decodeOption[Report](testJson)
    val transactionsList = optionJson.get.lines
    transactionsList.head.description should equal("Target")
    transactionsList.head.amount should equal(Some(99.99))
    transactionsList.head.category should equal("unknown")
    transactionsList.head.date should equal("2014-04-07")
  }

  test("bad JSON returns none") {
    val testJson = """ { "foo": [1, 2, 3] }"""
    val isValid = Parse.decodeOption[Report](testJson) match {
      case Some(t) => true
      case _ => false
    }
    isValid should equal(false)
  }

  test("should encode an error") {
    val err = GeorginaError("param error", "Your param is messed up")
    val expectedJson = """{"name":"param error","message":"Your param is messed up"}"""
    err.asJson.toString should equal(expectedJson)
  }

  test("should encode a date total class") {
    val dateTot = DateTotal("2014-05-20", Some(100.0))
    val expectedJson = """{"date":"2014-05-20","total":100}"""
    dateTot.asJson.toString should equal(expectedJson)
  }

  test("should encode a seq of DateTotals") {
    val rollup = List(
      DateTotal("2014-05-20", Some(100.0)),
      DateTotal("2014-05-21", Some(200.0))
    )
    val expectedJson = """[{"date":"2014-05-20","total":100},{"date":"2014-05-21","total":200}]"""
    rollup.asJson.toString should equal(expectedJson)
  }
}
