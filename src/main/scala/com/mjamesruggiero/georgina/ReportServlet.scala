package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
import com.mjamesruggiero.georgina.Utils._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalate.ScalateSupport
import scala.util.Try

class ReportServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/byday") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val ts = TransactionSet(Storage.inDateSpan(environment, start, end))
      val grouped = ts.timeSeriesSums.map {
        case(date, sum) => DateTotal(date, Some(sum))
      }.toList
      Ok(grouped.asJson)
    }
    catch {
      case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }
}
