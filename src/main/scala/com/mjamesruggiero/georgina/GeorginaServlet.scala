package com.mjamesruggiero.georgina

import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._
import com.mjamesruggiero.georgina.models._
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
    val ts = TransactionList(Storage.allTransactions(environment))
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
            Storage.storeTransaction(environment, JSONParsers.buildTransaction(t))
          }
        }
      }
      case _ => {
        InternalServerError(body="""unable to parse JSON""")
      }
    }
  }
}
