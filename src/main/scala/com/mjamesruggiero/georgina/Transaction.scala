package com.mjamesruggiero.georgina

case class Transaction(date: String, species: String, amount: Double, description: String)

class TransactionSet(transactions: List[Transaction]) {
  def averageAmount: Double = {
    val amounts = transactions.map(_.amount)
    amounts.sum / amounts.length
  }
}
