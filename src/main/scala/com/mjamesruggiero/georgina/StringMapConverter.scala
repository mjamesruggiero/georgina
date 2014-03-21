package com.mjamesruggiero.georgina

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class StringMapConverter(input: Map[String, String]) {
  def convert: Transaction = {
    val date = convertDate(input("date"))
    val amount = toDouble(input("amount")).getOrElse(0.00)
    Transaction(DateTime.parse("2013-03-29"), input("species"), amount, input("description"))
  }

  def toDouble(s: String):Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e:Exception => None
    }
  }

  def convertDate(datestring: String): Option[DateTime] = {
    val pattern = "dd/MM/yyyy"
    try {
      Some(DateTime.parse(datestring, DateTimeFormat.forPattern(pattern)))
    } catch {
      case e:Exception => None
    }
  }
}
