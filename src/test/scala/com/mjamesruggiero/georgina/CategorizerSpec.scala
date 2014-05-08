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
    "ALAMEDA ALLIANCE PAYROLL" -> "salary",
    "ALAMEDA GAS" -> "transportation",
    "ALAMEDA NATURAL" -> "grocery",
    "ALAMEDA POWER" -> "utilities",
    "ANTHROPOLOGIE" -> "entertainment",
    "ATM WITHDRAWAL - 2314 SANTA CLARA AVE ALAMEDA CA 6604 0008437" -> "atm",
    "AVEDA" -> "household",
    "BILL PAY ALAMEDA COUNTY I" -> "utilities",
    "BILL PAY ALAMEDA POWER AN ON-LINE xx834-01 ON 06-18" -> "utilities",
    "BILL PAY SBC" -> "utilities",
    "BOOKS INC" -> "entertainment",
    "BOUNCE" -> "clothing",
    "CA DMV" -> "transportation",
    "CHECK # 2045" -> "check",
    "CHECK CRD PURCHASE 06/18 AMAZON MKTPLACE PM AMZN.COM/BILL WA 446024XXXXXX1523 083169177896272 ?MCC=5942" -> "amazon",
    "CHECK DEPOSIT" -> "asset",
    "COLE HARDWARE" -> "household",
    "COSTPLUS" -> "household",
    "CREDOMOBILE" -> "utilities",
    "CVS" -> "household",
    "DANDELION FLOW" -> "entertainment",
    "DESTROY ALL SOFTWARE" -> "entertainment",
    "EAST BAY MUNICIP" -> "utilities",
    "EDWARD JONES" -> "savings",
    "ENCINAL MARKET" -> "grocery",
    "ENGINE WORKS" -> "transportation",
    "ETSY.COM" -> "entertainment",
    "FEEL GOOD BAKERY" -> "dining",
    "GITHUB" -> "utilities",
    "HSBC ONLINE TRANSFER" -> "savings",
    "IKEA" -> "household",
    "INTEREST PAYMENT" -> "interest",
    "JAZZERCISE" -> "health and exercise",
    "LAMORINDA SPANIS" -> "education",
    "LOVE AT FIRST BITE" -> "dining",
    "NETFLIX.COM" -> "entertainment",
    "NOB HILL" -> "grocery",
    "OFFICE MAX" -> "household",
    "OLD NAVY" -> "clothing",
    "PACIFIC GAS" -> "utilities",
    "PAYPAL" -> "paypal",
    "PEET'S" -> "grocery",
    "POPPY RED" -> "entertainment",
    "POS PURCHASE - NOB HILL 632 ALAMEDA CA 1523 00463171029909340" -> "grocery",
    "PURCHASE - KOHL" -> "household",
    "RAILSCASTS" -> "entertainment",
    "RECURRING TRANSFER" -> "savings",
    "REDBUBBLE" -> "clothes",
    "REILLY MEDIA" -> "entertainment",
    "RESTAURANT" -> "dining",
    "SAFEWAY" -> "grocery",
    "SHARETHROUGH INC DIRECT" -> "salary",
    "SHELL OIL" -> "transportation",
    "TARGET" -> "household",
    "THE MELT-EMBARCADE" -> "dining",
    "TJ MAXX" -> "clothing",
    "TO SAVINGS" -> "savings",
    "TOT TANK" -> "household",
    "TOY SAFARI" -> "entertainment",
    "TRADER JOE" -> "grocery",
    "TREASURY DIRECT" -> "savings",
    "VALERO" -> "transportation",
    "VISA WELLS" -> "credit card",
    "WALGREEN" -> "household",
    "WHOLE FOODS" -> "grocery",
    "WILDFLOWER CAFE" -> "dining",
    "WITHDRAWAL IN BRANCH" -> "cash",
    "YOSHI" -> "entertainment"
  )

  test("Categorizer should match basic examples") {
    for ((k, v) <- testMap) {
      val c = new Categorizer(k).categorize
      c should equal(Category(v))
    }
  }
}
