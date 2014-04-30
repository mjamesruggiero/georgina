package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalate.ScalateSupport

case class ServletException(message: String) extends Exception(message)

class GeorginaServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  def defaultDateParam: Map[String, String] = {
    val format = DateTimeFormat.forPattern("yyyy-MM-dd");
    val startOfThisMonth: DateTime = new DateTime().
      dayOfMonth().
      withMinimumValue()
    val startofNextMonth: DateTime = startOfThisMonth
      .plusMonths(1)
      .dayOfMonth()
      .withMinimumValue()
    Map(
      "startDate" -> startOfThisMonth.toString(format),
      "endDate" -> startofNextMonth.toString(format)
    )
  }

  get("/") {
    contentType="text/html"
    ssp("/georgina/index", "title" -> "Georgina")
  }

  get("/transactions") {
    val startParam = params.getOrElse("start", defaultDateParam("startDate"))
    val endParam = params.getOrElse("end", defaultDateParam("endDate"))

    val start = DateTime.parse(startParam)
    val end = DateTime.parse(endParam)

    val result = Storage.inDateSpan(environment, start, end)
    val ts = TransactionSet(result)
    Ok(ts.asJson)
  }

  get("/categories") {
    val startParam = params.getOrElse("start", defaultDateParam("startDate"))
    val endParam = params.getOrElse("end", defaultDateParam("endDate"))
    val start = DateTime.parse(startParam)
    val end = DateTime.parse(endParam)

    val data = Storage.categoryStats(environment, start, end)
    Ok(data.asJson)
  }

  get("/category/:category") {
    val startParam = params.getOrElse("start", defaultDateParam("startDate"))
    val endParam = params.getOrElse("end", defaultDateParam("endDate"))
    val start = DateTime.parse(startParam)
    val end = DateTime.parse(endParam)

    val data = Storage.withCategory(environment, params("category"), start, end)
    val ts = TransactionSet(data)
    Ok(ts.asJson)
  }

  post("/submit") {
    request.body.decodeOption[Report] match {
      case Some(t) => t.lines match {
        case Nil => {
          logger.error("failed to parse line ${t.lines}")
          InternalServerError(body =
            s"""unable to parse JSON"""
          )
        }
        case (l:List[Line]) => {
          l.map { t =>
            Storage.store(environment, JSONParsers.buildTransaction(t))
          }
        }
      }
      case _ => {
        InternalServerError(body="""unable to parse JSON""")
      }
    }
  }
}
