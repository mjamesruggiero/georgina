package com.mjamesruggiero.georgina

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class StringMapConverter(input: Map[String, String]) {
  def convert: Option[Transaction] = {
    isValid(input) match {
      case true => {
        val date = convertDate(input("date"))
        val amount = toDouble(input("amount")).getOrElse(0.00)

        Some(Transaction(date.getOrElse(DateTime.now), input("species"), amount, input("description")))
      }
      case false => None
    }
  }

  private def toDouble(s: String): Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e:Exception => None
    }
  }

  private def convertDate(datestring: String): Option[DateTime] = {
    val pattern = "MM/dd/yyyy"
    try {
      Some(DateTime.parse(datestring, DateTimeFormat.forPattern(pattern)))
    } catch {
      case e:Exception => None
    }
  }

  private def isValid(rawMap: Map[String, String]): Boolean = {
    rawMap.values.map(_.trim).forall(_.length > 0)
  }
}
