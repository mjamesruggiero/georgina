package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalate.ScalateSupport

case class ServletException(message: String) extends Exception(message)

class GeorginaServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  val logger =  LoggerFactory.getLogger(getClass)
  import com.mjamesruggiero.georgina.JSONParsers._

  get("/") {
    contentType="text/html"
    ssp("/georgina/index", "title" -> "Georgina")
  }

  get("/transactions") {
    val startParam = params.getOrElse("start", "2014-01-01")
    val endParam = params.getOrElse("end", "2014-06-01")
    val start = DateTime.parse(startParam)
    val end = DateTime.parse(endParam)

    val result = Storage.inDateSpan(environment, start, end)
    val ts = TransactionSet(result)
    Ok(ts.asJson)
  }

  get("/categories/:category") {
    val data = Storage.withCategory(environment, params("category"))
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
