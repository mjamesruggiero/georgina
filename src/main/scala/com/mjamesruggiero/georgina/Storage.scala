package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina.config._
import com.mjamesruggiero.georgina.models._
import org.joda.time.DateTime
import scalaz.Reader._
import scala.util.{Success, Failure}

case class QueryException(message:String) extends Exception(message)

case class CategorySummary(category: String, count: Int, mean: Double, sttdev: Double)

object Storage {

  import DB._
  lazy val transactionRowConverter = Map(
      "id" -> mkLong,
      "date" -> mkString,
      "species" -> mkString,
      "amount" -> mkDouble,
      "category" -> mkString,
      "description" -> mkString
    )

  def storeLinesAsTransactions(lines: List[Line], config: DBConfig) = {
    val config = TestDatabase
    val transactions = lines.map { l => JSONParsers.buildTransaction(l) }
    val nonExistentTransactions = transactions.filter(! exists(_, config))
    nonExistentTransactions.map { t => store(t, config) }
  }

  def store(t: Transaction, config: DBConfig) = {
    t match {
      case Transaction(id, date, species, amt, cat, desc) => {
        if (! exists(t, config)) {
          val validCat = new Categorizer(desc).categorize.c
          val validSpecies = if (amt < 0.0) "debit" else "asset"
          val q = s"""
            INSERT INTO transactions(id, date, species,
              description, category, amount)
            VALUES (null, '${Utils.canonicalDate(date)}',
              '${validSpecies}', '${desc}', '${validCat}', ${amt})"""
          DB.update(q, config)
        }
      }
      case _ => false
    }
  }

  def exists(t: Transaction, config: DBConfig): Boolean = {
    val q = s"""SELECT COUNT(*) AS count
          FROM transactions
          WHERE date= '${t.date}'
          AND species= '${t.species}'
          AND description= '${t.description}'
          AND amount=${t.amount}"""
    val result = query(q, Map("count" -> mkInt), config) map { row =>
      row.get("count").fold(0)(asInt)
    }
    result.headOption.getOrElse(0) > 0
  }

  def all(config: DBConfig): List[Transaction] = {
    val q = """SELECT id, date, species, amount, category, description
          FROM transactions ORDER BY date, amount DESC"""

    query(q, transactionRowConverter, config) map { row =>
      Transaction(
        row.get("id").fold(0L)(asLong),
        DateTime.parse(row.get("date").fold("")(asString)),
        row.get("species").fold("")(asString),
        row.get("amount").fold(0.0)(asDouble),
        row.get("category").fold("")(asString),
        row.get("description").fold("")(asString)
      )
    }
  }

  def withCategory(category: String, start: DateTime, end: DateTime, config: DBConfig): List[Transaction] = {
    val queryString = s"""SELECT id, date, species, amount, category, description
    FROM transactions
    WHERE category = '${category}'
    AND date >= '${Utils.canonicalDate(start)}'
    AND date <= '${Utils.canonicalDate(end)}'
    ORDER BY date DESC"""

    query(queryString, transactionRowConverter, config) map { row =>
      Transaction(
        row.get("id").fold(0L)(asLong),
        DateTime.parse(row.get("date").fold("")(asString)),
        row.get("species").fold("")(asString),
        row.get("amount").fold(0.0)(asDouble),
        row.get("category").fold("")(asString),
        row.get("description").fold("")(asString)
        )
    }
  }

  def inDateSpan(startDate: DateTime, endDate: DateTime, config: DBConfig) = {
    val sql = s"""SELECT id, date, species, amount, category, description
    FROM transactions
    WHERE date >= '${Utils.canonicalDate(startDate)}'
    AND date <= '${Utils.canonicalDate(endDate)}'
    ORDER BY date DESC"""
    query(sql, transactionRowConverter, config) map { row =>
      Transaction(
        row.get("id").fold(0L)(asLong),
        DateTime.parse(row.get("date").fold("")(asString)),
        row.get("species").fold("")(asString),
        row.get("amount").fold(0.0)(asDouble),
        row.get("category").fold("")(asString),
        row.get("description").fold("")(asString)
      )
    }
  }

  def categoryStats(startDate: DateTime,
                    endDate: DateTime,
                    config: DBConfig): List[CategorySummary] = {

    val queryString = s"""SELECT category, COUNT(*) category_count,
    AVG(amount) mean, STD(amount) stddev
    FROM transactions
    WHERE date >= '${Utils.canonicalDate(startDate)}'
    AND date <= '${Utils.canonicalDate(endDate)}'
    GROUP BY category
    ORDER by category_count DESC"""
    val resultMap = Map(
      "category" -> mkString,
      "category_count" -> mkInt,
      "mean" -> mkDouble,
      "stddev" -> mkDouble
    )
    query(queryString, resultMap, config) map { row =>
      CategorySummary(
        row.get("category").fold("")(asString),
        row.get("category_count").fold(0)(asInt),
        row.get("mean").fold(0.0)(asDouble),
        row.get("stddev").fold(0.0)(asDouble)
      )
    }
  }

  def getById(id: Int, config: DBConfig): Option[Transaction] = {
    val q = s"""SELECT id, date, species, amount, category, description
          FROM transactions WHERE id = ${id}"""

    query(q, transactionRowConverter, config). map { row =>
      Transaction(
        row.get("id").fold(0L)(asLong),
        DateTime.parse(row.get("date").fold("")(asString)),
        row.get("species").fold("")(asString),
        row.get("amount").fold(0.0)(asDouble),
        row.get("category").fold("")(asString),
        row.get("description").fold("")(asString)
        )
    }.headOption
  }

  def update(t: Transaction, config: DBConfig): Boolean = {
    t match {
      case Transaction(id, date, species, amt, cat, desc) => {
        val validCat = new Categorizer(desc).categorize.c
        val validSpecies = if (amt < 0.0) "debit" else "asset"
        val q = s"""UPDATE transactions SET
              date = '${Utils.canonicalDate(date)}',
              species = '${validSpecies}',
              description = '${desc}',
              category = '${validCat}',
              amount = ${amt}
              WHERE id = ${id}"""
        // TODO add some logging for failure case
        DB.update(q, TestDatabase) match {
          case Success(_) => true
          case Failure(ex) => false
        }
      }
      case _ => false
    }
  }

  def byWeek(config: DBConfig): List[WeekSummary] = {
    val q = """SELECT FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7)) AS week_beginning,
          SUM(amount) AS total,
          COUNT(*) AS count
          FROM transactions
          WHERE species = 'debit'
          GROUP BY FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7))
          ORDER BY FROM_DAYS(TO_DAYS(date) -MOD(TO_DAYS(date) -1, 7)) DESC"""
    val resultMap = Map(
      "week_beginning" -> mkString,
      "total" -> mkDouble,
      "count" -> mkInt
    )
    query(q, resultMap, config) map { row =>
      WeekSummary(
        row.get("week_beginning").fold("")(asString),
        row.get("total").fold(0.0)(asDouble),
        row.get("count").fold(0)(asInt)
      )
    }
  }
}
