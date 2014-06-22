import com.mjamesruggiero.georgina._

import org.scalatra._
import javax.servlet.ServletContext
import scalikejdbc._
import com.mjamesruggiero.georgina.config._

class ScalatraBootstrap extends LifeCycle with DatabaseClientInit {
  override def init(context: ServletContext) {
    configureDatabaseClient
    val env = DevelopmentDatabase
    context.mount(new CategoryServlet(env), "/categories/*")
    context.mount(new IndexServlet(env), "/*")
    context.mount(new ReportServlet(env), "/reports/*")
    context.mount(new TestServlet(env), "/test/*")
    context.mount(new TransactionServlet(env), "/transactions/*")
  }
}
