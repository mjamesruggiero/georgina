import com.mjamesruggiero.georgina._

import org.scalatra._
import javax.servlet.ServletContext
import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.controllers._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val env = DevelopmentDatabase
    context.mount(new CategoryController(env), "/categories/*")
    context.mount(new IndexController(env), "/*")
    context.mount(new ReportController(env), "/reports/*")
    context.mount(new TestController(env), "/test/*")
    context.mount(new TransactionController(env), "/transactions/*")
  }
}
