package com.mjamesruggiero.georgina

import org.scalatra._
import scalate.ScalateSupport

class GeorginaServlet extends GeorginaStack {

  get("/") {
    <html>
      <body>
        <h1>This is georgina</h1>
        <p><a href="hello-scalate">Scalate</a></p>
      </body>
    </html>
  }
}
