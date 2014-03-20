package com.mjamesruggiero.georgina

class Parser(raw: String) {
  def parse = {
    val quoteless = withoutQuotes(raw)
    val dummyMap = Map("date" -> None, 
                       "amount" -> None, 
                       "description" -> None, 
                       "species" -> None)

    val splitLines = quoteless.split(",") match {
      case Array(date, rawAmount, _, _, description) => (date, rawAmount, description)
      case _ => quoteless
    }

    splitLines match {
      case (date, rawAmount, description) => Map("date" -> date, 
                                                 "amount" -> fixNegativeAmount(rawAmount.toString), 
                                                 "description" -> description, 
                                                 "species" -> getSpecies(rawAmount.toString))
      case _ => dummyMap 
    }
  }

  def withoutQuotes(input: String): String  = {
    val quotePattern = "\"".r
    quotePattern.replaceAllIn(raw, "")
  }

  def fixNegativeAmount(rawAmount: String) =
    if (rawAmount.startsWith("-")) rawAmount.drop(1) else rawAmount

  def getSpecies(rawAmount: String) = 
    if (rawAmount.startsWith("-")) "debit" else "asset"
}

