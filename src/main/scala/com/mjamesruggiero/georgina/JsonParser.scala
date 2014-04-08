package com.mjamesruggiero.georgina

import argonaut._, Argonaut._

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
}
