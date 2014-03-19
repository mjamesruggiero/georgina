package com.mjamesruggiero.georgina

class Parser(raw: String) {
  val quotePattern = "\"".r
  val leadingSignPattern = """-""".r

  def withoutQuotes = quotePattern.replaceAllIn(raw, "")
  def description = withoutQuotes.split(",").last
  def date = withoutQuotes.split(",")(0)
  def amount = {
    val amount = withoutQuotes.split(",")(1)
    leadingSignPattern.replaceAllIn(amount, "")
  }
}

