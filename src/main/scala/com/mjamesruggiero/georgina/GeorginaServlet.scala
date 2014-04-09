package com.mjamesruggiero.georgina

import org.scalatra._
import scalate.ScalateSupport
import argonaut._, Argonaut._

class GeorginaServlet extends GeorginaStack with ScalateSupport {

  import com.mjamesruggiero.georgina.JSONParsers._
  val users = Storage.init
  val names = users.map(_("FIRST_NAME")).mkString(", ")

  get("/") {
    contentType="text/html"

    ssp("/georgina/index", "title" -> "Georgina", "names" -> names)
  }

  post("/submit") {
    request.body.decodeOption[TransactionPost] match {
      case Some(t) => t.lines match {
        case Nil => {
          InternalServerError(body =
            s"""unable to parse JSON"""
          )
        }
        case (l:List[Line]) => Ok("thanks!")
      }
      case _ => {
        InternalServerError(body="""unable to parse JSON""")
      }
    }
  }
}
