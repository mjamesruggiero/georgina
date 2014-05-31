package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra._
import scalate.ScalateSupport

class IndexServlet(environment: String = "development")  extends GeorginaStack with ScalateSupport {

  get("/") {
    contentType="text/html"
    ssp("/georgina/index", "subhead" -> "she counts your money", "title" -> "Georgina")
  }
}
