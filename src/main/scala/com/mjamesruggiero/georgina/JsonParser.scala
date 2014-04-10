package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import org.joda.time.DateTime

case class TransactionPost(lines: List[Line])

// i.e "just a line in a file"
case class Line(
  date: String,
  amount: Option[Double],
  description: String
)

object JSONParsers {
  implicit def LineCodecJson =
    casecodec3(Line.apply, Line.unapply)("date", "amount", "description")

  implicit def TransactionPostJsonCodec =
    casecodec1(TransactionPost.apply, TransactionPost.unapply)("transactions")

  def buildTransaction(line: Line): Transaction = {
    val parsedDate = DateTime.parse(line.date)
    Transaction(parsedDate, "debit", line.amount.getOrElse(0.0), line.description)
  }
}
