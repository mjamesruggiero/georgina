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
  category: String,
  description: String
)

case class DateTotal(date: String, total: Option[Double])

case class WeekSummary(date: String, total: Double, count: Int)

case class GeorginaError(name: String, message: String)

object JSONParsers {
  implicit def LineCodecJson =
    casecodec4(Line.apply, Line.unapply)("date", "amount", "category", "description")

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
    casecodec6(Transaction.apply, Transaction.unapply)(
      "id",
      "date",
      "species",
      "amount",
      "category",
      "description"
    )

  def buildTransaction(line: Line): Transaction = {
    val parsedDate = DateTime.parse(line.date)
    Transaction(0L, parsedDate, "debit", line.amount.getOrElse(0.0), line.category, line.description)
  }

  implicit def CategorySummaryCodec: CodecJson[CategorySummary] =
    casecodec4(CategorySummary.apply, CategorySummary.unapply)(
      "category",
      "count",
      "mean",
      "standard_deviation")

  implicit def GeorginaErrorCodec: CodecJson[GeorginaError]  =
    casecodec2(GeorginaError.apply, GeorginaError.unapply)("name", "message")

  implicit def DateTotalCodec: CodecJson[DateTotal]  =
    casecodec2(DateTotal.apply, DateTotal.unapply)("date", "total")

  implicit def WeekSummaryCOdec: CodecJson[WeekSummary]  =
    casecodec3(WeekSummary.apply, WeekSummary.unapply)("week_beginning", "total", "count")
}
