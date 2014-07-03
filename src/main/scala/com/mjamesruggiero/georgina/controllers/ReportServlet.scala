package com.mjamesruggiero.georgina.controllers

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina.Utils._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scala.util.Try
import scalate.ScalateSupport

class ReportServlet(config: DBConfig)  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/byday") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val ts = TransactionSet(Storage.inDateSpan(start, end, config))
      val grouped = ts.timeSeriesSumsWithDefaultZeros.map {
        case(date, sum) => DateTotal(date, Some(sum))
      }.toList
      Ok(grouped.asJson)
    }
    catch {
      case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }

  get("/byweek") {
    val rows = Storage.byWeek(config: DBConfig)
    Ok(rows.asJson)
  }
}

