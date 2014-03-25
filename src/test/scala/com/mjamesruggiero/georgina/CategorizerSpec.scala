package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite

class CategorizerSpec extends ScalatraSuite with FunSuite {
  test("Categorizer should return unknown for unknowns") {
    val c = new Categorizer("foo").categorize
    c should equal(Category("unknown"))
  }

  test("Categorizer should return something meaningful for known strings") {
    val c = new Categorizer("POS PURCHASE - ALAMEDA NATURAL GR ALAMEDA CA 1523 00463088617812950").categorize
    c should equal(Category("grocery"))
  }

  val testMap = Map(
    "check" -> "CHECK # 2045",
    "grocery" -> "POS PURCHASE - NOB HILL 632 ALAMEDA CA 1523 00463171029909340",
    "atm" -> "ATM WITHDRAWAL - 2314 SANTA CLARA AVE ALAMEDA CA 6604 0008437",
    "amazon" -> "CHECK CRD PURCHASE 06/18 AMAZON MKTPLACE PM AMZN.COM/BILL WA 446024XXXXXX1523 083169177896272 ?MCC=5942",
    "utilities" -> "BILL PAY ALAMEDA POWER AN ON-LINE xx834-01 ON 06-18"
  )

  test("Categorizer should match basic examples") {
    for ((k, v) <- testMap) {
      val c = new Categorizer(v).categorize  
      c should equal(Category(k))
    }
  }
}
