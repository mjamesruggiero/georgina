package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import org.scalatest.fixture.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import scalikejdbc.SQLInterpolation._
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback

class AutoRollbackSpec extends FlatSpec with AutoRollback with ShouldMatchers with BeforeAndAfter {

  lazy val config = new TestEnv

  before {
    DBsWithEnv(config.env).setupAll()
    ConnectionPool('default).borrow()
  }

  override def fixture(implicit session: DBSession) {
    sql"insert into transactions values (NULL, ${DateTime.now}, 'debit', 'Github', 'personal', 20.00)".update.apply()
  }

  it should "store a new transaction" in { implicit session =>
    val returned: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      .map(rs => rs.int("count"))
      .single.apply()
    val countBefore = returned match {
      case Some(num) => num
      case _ => 0
    }

    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Office Depot")
    Storage.storeTransaction(config.env, t)
    val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count FROM transactions"""
      .map(rs => rs.int("count"))
      .single.apply()
    val countAfter = postHoc match {
      case Some(num) => num
      case _ => 0
    }
    countAfter should equal(countBefore + 1)
  }

  it should "create a new record only if one does not exist" in { implicit session =>
    val t = new Transaction(1L, DateTime.now, "debit", 20.00, "unknown", "Github")
    val returned: Option[Int] = sql"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date=${t.date}
          AND species=${t.species}
          AND description=${t.description}
          AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    val countBefore = returned match {
      case Some(num) => num
      case _ => 0
    }
    Storage.storeTransaction(config.env, t)

    val postHoc: Option[Int] = sql"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date=${t.date}
          AND species=${t.species}
          AND description=${t.description}
          AND amount=${t.amount}""".map(rs => rs.int("count")).single.apply()

    val countAfter = postHoc match {
      case Some(num) => num
      case _ => 0
    }
    countAfter should equal(countBefore)
  }
}
