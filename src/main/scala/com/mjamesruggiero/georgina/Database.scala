package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import java.math.BigDecimal
import java.sql.{ DriverManager, ResultSet }
import scala.util.{ Try, Success, Failure }
import scalaz.concurrent.Task
import scalaz.stream.{ io, Process }

trait Database {
  type Row = Map[String, Any]
  def query(queryString: String, columnMapping: Map[String, (ResultSet, String) => Any], config: DBConfig): List[Row]
}

object DB extends Database {
  def query(
    queryString: String,
    columnMapping: Map[String, (ResultSet, String) => Any],
    config: DBConfig
  ): List[Row] = {
    Class.forName(config.driverClassName).newInstance
    Try {
      lazy val conn = DriverManager.getConnection(
        config.address,
        config.username,
        config.password
      )
      lazy val statement = conn.createStatement(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_READ_ONLY
      )
      val out = processRows(
        statement.executeQuery(queryString),
        columnMapping
      ).runLog.run.toList
      conn.close()
      out
    } match {
      case Success(o) => o
      case Failure(exp) => List.empty[Row]
    }
  }

  def update(queryString: String, config: DBConfig) = {
    Class.forName(config.driverClassName).newInstance
    Try {
      lazy val conn = DriverManager.getConnection(
        config.address,
        config.username,
        config.password
      )
      lazy val statement = conn.createStatement(
        ResultSet.TYPE_FORWARD_ONLY,
        ResultSet.CONCUR_READ_ONLY
      )
      statement.executeUpdate(queryString)
      conn.close()
    }
  }

  // Column mapping functions for query definition
  val mkBoolean     = (rs: ResultSet, k: String) => rs.getBoolean(k)
  val mkInt         = (rs: ResultSet, k: String) => rs.getInt(k)
  val mkLong        = (rs: ResultSet, k: String) => rs.getLong(k)
  val mkBigDecimal  = (rs: ResultSet, k: String) => rs.getBigDecimal(k)
  val mkFloat       = (rs: ResultSet, k: String) => rs.getFloat(k)
  val mkDouble      = (rs: ResultSet, k: String) => rs.getDouble(k)
  val mkString      = (rs: ResultSet, k: String) => rs.getString(k)

  // Mapping functions for result parsing
  val asString     = (c: Any) => c match { case(s: String)     => s }
  val asDouble     = (c: Any) => c match { case(d: Double)     => d }
  val asFloat      = (c: Any) => c match { case(f: Float)      => f }
  val asBigDecimal = (c: Any) => c match { case(b: BigDecimal) => b }
  val asLong       = (c: Any) => c match { case(l: Long)       => l }
  val asInt        = (c: Any) => c match { case(i: Int)        => i }
  val asBoolean    = (c: Any) => c match { case(b: Boolean)    => b }

  private def processRows(
    rs: ResultSet,
    m: Map[String, (ResultSet, String) => Any]): Process[Task, Map[String, Any]] = {
    io.resource(Task.delay(rs))(rs => Task.delay(rs.close)) { rs =>
      Task.delay {
        if (rs.next) m map { case(k, fn) =>
          k -> fn(rs, k)
        } toMap else throw Process.End
      }
    }
  }
}
