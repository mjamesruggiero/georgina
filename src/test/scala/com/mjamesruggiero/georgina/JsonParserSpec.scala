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
          "description":"Target"
        },
        {
          "date":"2014-04-04", 
          "amount": 20.00, 
          "description":"Amazon.com"
        }
      ]
    }
    """
    val returned = testJson.decodeOption[TransactionPost] match {
      case(Some(tp)) => Some(tp)
      case _ => None
    }
    val lines = for (t <- returned) yield (t.lines.head)
    val properties = for (l <- lines) yield (l.description, l.amount, l.date)

    properties.get._3 should equal("2014-04-07")
    properties.get._2 should equal(Some(99.99))
    properties.get._1 should equal("Target")
  }
}
