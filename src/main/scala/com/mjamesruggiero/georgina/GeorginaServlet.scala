package com.mjamesruggiero.georgina

import org.scalatra._
import scalate.ScalateSupport
import argonaut._, Argonaut._
import com.mjamesruggiero.georgina._

case class ServletException(message: String) extends Exception(message)

class GeorginaServlet extends GeorginaStack with ScalateSupport {

  import com.mjamesruggiero.georgina.JSONParsers._

  get("/") {
    contentType="text/html"

    ssp("/georgina/index", "title" -> "Georgina")
  }

  post("/submit") {
    request.body.decodeOption[TransactionPost] match {
      case Some(t) => t.lines match {
        case Nil => {
          InternalServerError(body =
            s"""unable to parse JSON"""
          )
        }
        case (l:List[Line]) => {
          Storage.storeTransaction(JSONParsers.buildTransaction(l.head))
        }
      }
      case _ => {
        InternalServerError(body="""unable to parse JSON""")
      }
    }
  }
}
