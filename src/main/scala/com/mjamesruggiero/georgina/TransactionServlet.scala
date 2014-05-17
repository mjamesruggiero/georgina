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

class TransactionServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/") {
    contentType="text/html"
    ssp("/georgina/index", "subhead" -> "she counts your money", "title" -> "Georgina")
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
          Storage.storeLinesAsTransactions(environment, l)
        }
      }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }

  post("/") {
    request.body.decodeOption[Line] match {
      case Some(t) => { Storage.store(environment, JSONParsers.buildTransaction(t)) }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }
}