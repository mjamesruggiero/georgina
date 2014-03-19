package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import org.joda.time.DateTime

class ParserSpec extends ScalatraSuite with FunSuite {
  val testLine  = """03/29/2013","-35.70","*","","POS PURCHASE - ALAMEDA NATURAL GR ALAMEDA CA 1523 00463088617812950"""
  val parser = new Parser(testLine)

  test("Parser should parse description") {
    parser.description should equal("POS PURCHASE - ALAMEDA NATURAL GR ALAMEDA CA 1523 00463088617812950")
  }

  test("Parser should parse date") {
    parser.date should equal("03/29/2013")
  }

  test("Parser should parse amount") {
    parser.amount should equal("35.70")
  }
}
