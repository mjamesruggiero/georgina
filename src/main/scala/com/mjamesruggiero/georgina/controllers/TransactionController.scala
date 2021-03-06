package com.mjamesruggiero.georgina.controllers

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import com.mjamesruggiero.georgina.Utils._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalate.ScalateSupport
import scala.util.Try

class TransactionController(config: DBConfig)  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val result = Storage.inDateSpan(start, end, config)
      val ts = TransactionSet(result).transactions.toList
      Ok(ts.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }

  get("/:id") {
    val transaction = Storage.getById(params("id").toInt, config)
    Ok(transaction.asJson)
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
          Storage.storeLinesAsTransactions(l, config)
          status(201)
        }
      }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }

  post("/") {
    request.body.decodeOption[Line] match {
      case Some(t) => { Storage.store(JSONParsers.buildTransaction(t), config) }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }

  put("/:id") {
    request.body.decodeOption[Line] match {
      case Some(line) => {
        val parsedDate = DateTime.parse(line.date)
        val t = Transaction(params("id").toLong, parsedDate, "debit", line.amount.getOrElse(0.0), line.category, line.description)
        Storage.update(t, config)
      }
      case _ => {
        InternalServerError(GeorginaError("format error", "unable to parse JSON").asJson)
      }
    }
  }
}
