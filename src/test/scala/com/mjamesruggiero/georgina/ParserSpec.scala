package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import org.joda.time.DateTime

class ParserSpec extends ScalatraSuite with FunSuite {
  val testDebitLine  = """03/29/2013","-35.70","*","","POS PURCHASE - ALAMEDA NATURAL GR ALAMEDA CA 1523 00463088617812950"""
  val badLine = """laksdhoiuyvaibe jhdofkwjnq ldia spoiwef poqih2iu12oi371y29487y987q"""
  val parser = new Parser(testDebitLine)

  test("Parser should parse description") {
    parser.parse("description") should equal("POS PURCHASE - ALAMEDA NATURAL GR ALAMEDA CA 1523 00463088617812950")
  }

  test("Parser should parse date") {
    parser.parse("date") should equal("03/29/2013")
  }

  test("Parser should parse amount") {
    parser.parse("amount") should equal("35.70")
  }

  test("Parser knows debits") {
    parser.parse("species") should equal("debit")
  }
  
  test("Parser knows assets") {
    val testAssetLine = """03/29/2013","100.70","*","","BIG COMPANY CHECK"""
    val parser = new Parser(testAssetLine)
    parser.parse("species") should equal("asset")
  }

  test("bad lines should return None values"){
    val failingParser = new Parser(badLine)
    failingParser.parse("date") should equal(None)
    failingParser.parse("amount") should equal(None)
    failingParser.parse("description") should equal(None)
  }
}
