package com.mjamesruggiero.georgina.models

import com.mjamesruggiero.georgina.Utils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scalikejdbc._, SQLInterpolation._

case class Transaction(
id: Long,
date: DateTime,
species: String,
amount: Double,
category: String,
description: String)

object Transaction extends SQLSyntaxSupport[Transaction] {
  override val tableName = "transactions"
  def apply(rs: WrappedResultSet) = new Transaction(
    rs.long("id"), rs.dateTime("date"), rs.string("species"), rs.double( "amount" ), rs.string("category"), rs.string("description"))
}

object Joda {
    implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
}

case class TransactionSet(transactions: List[Transaction]) {
  import Joda._

  val format = DateTimeFormat.forPattern("yyyy-MM-dd");

  def averageAmount: Double = {
    val amounts = transactions.map(_.amount)
    amounts.sum / amounts.length
  }

  def amountBySpecies = {
    transactions.groupBy(_.species).map(
      { case  (name, xs) => (name, xs.map(_.amount).sum ) }
    )
  }

  def average(species: String): Double = {
    val count = withSpecies(species).length
    amountBySpecies(species) / count
  }

  def averageSpend = average("debit")

  def standardDeviation(species: String): Double = {
    def mean(values: List[Double]) = {
      if(values.length < 0) 0 else values.sum / values.length
    }

    def variance(values: List[Double]) = {
      val mu = mean(values)
      val deviations = for {
        v <- values
        dev = (v - mu) * (v - mu)
      } yield dev
      mean(deviations)
    }

    val values = transactions.filter(_.species == species).map(_.amount)
    math.sqrt(variance(values))
  }

  def withDescription(description: String) : List[Transaction] = {
    transactions.filter(_.description == description)
  }

  def withSpecies(species: String): List[Transaction] = {
    transactions.filter(_.species == species)
  }

  def debits: List[Transaction] = {
    withSpecies("debit")
  }

  def byDate = transactions.groupBy(_.date)

  def timeSeriesSums: Seq[(String, Double)] = timeSeriesMap.toSeq.sortBy(_._1)

  def timeSeriesMap: Map[String, Double] =
    withSpecies("debit").groupBy(_.date).map {
      case(date, t) => (date.toString(format) -> (t.map(_.amount * -1)).sum )
    }

  /**
   * n.b this does not handle an empty datbase result for the sequence;
   * e.g., say the date span is 4 days, and there were no transactions:
   * ordered would be an empty list.
   * This is an API issue w/r/t transaction set being a wrapper
   * around a query result
   */
  def timeSeriesSumsWithDefaultZeros: Seq[(String, Double)] = {
    val records = withSpecies("debit").groupBy(_.date).map {
      case(date, t) => (date.toString(format) -> (t.map(_.amount * -1)).sum )
    }

    val ordered = withSpecies("debit").map((t: Transaction) => t.date).sorted
    val withDefaults = ordered match {
      case Seq(a, rest @ _ *) => Utils.getDatesBetween(a, rest.last).map((d: DateTime) => (d.toString(format), 0.0)).toMap
      case _ => Map.empty[String, Double]
    }
    Utils.mergeMapWithDefaults(withDefaults, records).toSeq.sortBy(_._1)
  }
}
