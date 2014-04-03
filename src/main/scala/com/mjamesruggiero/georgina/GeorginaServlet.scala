package com.mjamesruggiero.georgina

import org.scalatra._
import scalate.ScalateSupport

class GeorginaServlet extends GeorginaStack with ScalateSupport {

  val users = Storage.init
  val names = users.map(_("FIRST_NAME")).mkString(", ")

  get("/") {
    contentType="text/html"

    ssp("/georgina/index", "title" -> "Georgina", "names" -> names)
  }
}
