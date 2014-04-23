package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import org.joda.time.DateTime
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
import org.joda.time.format.ISODateTimeFormat

case class Report(lines: List[Line])

// i.e "just a line in a file"
case class Line(
  date: String,
  amount: Option[Double],
  description: String
)

object JSONParsers {
  implicit def LineCodecJson =
    casecodec3(Line.apply, Line.unapply)("date", "amount", "description")

  implicit def ReportJsonCodec =
    casecodec1(Report.apply, Report.unapply)("transactions")

  implicit def TransactionSetCodec: CodecJson[TransactionSet]  =
    casecodec1(TransactionSet.apply, TransactionSet.unapply)("transactions")

  lazy val FULL_ISO8601_FORMAT = ISODateTimeFormat.dateTime

  implicit def DateTimeAsISO8601EncodeJson: EncodeJson[org.joda.time.DateTime] =
    EncodeJson(s => jString(s.toString(FULL_ISO8601_FORMAT)))

   implicit def DateTimeAsISO8601DecodeJson: DecodeJson[DateTime] =
    implicitly[DecodeJson[String]].map(FULL_ISO8601_FORMAT.parseDateTime) setName "org.joda.time.DateTime"

  implicit def TransactionCodec: CodecJson[Transaction]  =
    casecodec4(Transaction.apply, Transaction.unapply)(
      "date",
      "species",
      "amount",
      "description"
    )

  def buildTransaction(line: Line): Transaction = {
    val parsedDate = DateTime.parse(line.date)
    Transaction(parsedDate, "debit", line.amount.getOrElse(0.0), line.description)
  }
}
