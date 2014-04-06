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
}
