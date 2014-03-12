package com.mjamesruggiero.georgina

case class Transaction(date: String, species: String, amount: Double, description: String)

class TransactionSet(transactions: List[Transaction]) {
  def averageAmount: Double = {
    val amounts = transactions.map(_.amount)
    amounts.sum / amounts.length
  }

  def amountBySpecies = {
    transactions.groupBy(_.species).map(
      { case  (name, xs) => (name, xs.map(_.amount).sum ) } 
    )
  }

  def averageSpend: Double = {
    val amount = amountBySpecies("debit")
    val count = transactions.filter(_.species == "debit").length
    amount / count
  }
}
