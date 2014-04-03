import com.mjamesruggiero.georgina._

import org.scalatra._
import javax.servlet.ServletContext
import scalikejdbc._

class ScalatraBootstrap extends LifeCycle with DatabaseClientInit {
  override def init(context: ServletContext) {
    configureDatabaseClient
    context.mount(new GeorginaServlet, "/*")
  }
}
