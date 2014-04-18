package com.mjamesruggiero.georgina.models

import org.joda.time.DateTime

case class Transaction(date: DateTime,
species: String,
amount: Double,
description: String)

case class TransactionList(transactions: List[Transaction])

class TransactionSet(val transactions: List[Transaction]) {
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
}
