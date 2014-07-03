package com.mjamesruggiero.georgina.controllers

import com.mjamesruggiero.georgina.config._
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

case class ServletException(message: String) extends Exception(message)

class CategoryServlet(config: DBConfig)  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val data = Storage.categoryStats(start, end, config)
      Ok(data.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "error: invalid params").asJson)
    }
  }

  get("/:category") {
    try {
      val start = DateTime.parse(params.getOrElse("start", defaultDateParam("startDate")))
      val end = DateTime.parse(params.getOrElse("end", defaultDateParam("endDate")))
      val data = Storage.withCategory(params("category"), start, end, config)
      val ts = TransactionSet(data)
      Ok(ts.asJson)
    }
      catch {
        case _: Throwable => InternalServerError(GeorginaError("param error", "improper start and end dates").asJson)
    }
  }

  //get("/categories/:id") {
  //}

  //post("/") {
  //}

  //put("/categories/:id") {
  //}

  //delete("/categories/:id") {
  //}
}
