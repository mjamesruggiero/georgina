package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalate.ScalateSupport
import scala.util.Try

case class ServletException(message: String) extends Exception(message)

class GeorginaServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  def defaultDateParam: Map[String, String] = {
    val format = DateTimeFormat.forPattern("yyyy-MM-dd");

    val startOfThisMonth: DateTime = new DateTime().
      dayOfMonth().withMinimumValue()

    val startofNextMonth: DateTime = startOfThisMonth
      .plusMonths(1).dayOfMonth().withMinimumValue()

    Map(
      "startDate" -> startOfThisMonth.toString(format),
      "endDate" -> startofNextMonth.toString(format)
    )
  }

  get("/") {
    contentType="text/html"
    ssp("/georgina/index", "subhead" -> "This is the servlet", "title" -> "Georgina")
  }

  get("/transactions") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val result = Storage.inDateSpan(environment, start, end)
      val ts = TransactionSet(result)
      Ok(ts.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }

  get("/categories") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val data = Storage.categoryStats(environment, start, end)
      Ok(data.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }

  get("/category/:category") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val data = Storage.withCategory(environment, params("category"), start, end)
      val ts = TransactionSet(data)
      Ok(ts.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "improper start and end dates").asJson)
    }
  }

  post("/submit") {
    request.body.decodeOption[Report] match {
      case Some(t) => t.lines match {
        case Nil => {
          logger.error("failed to parse line ${t.lines}")
          InternalServerError(
            GeorginaError("format error", "unable to parse JSON").asJson
          )
        }
        case (l:List[Line]) => {
          l.map { t =>
            Storage.store(environment, JSONParsers.buildTransaction(t))
          }
        }
      }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }
}
