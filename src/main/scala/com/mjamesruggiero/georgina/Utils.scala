package com.mjamesruggiero.georgina

import com.google.common.hash._
import com.mjamesruggiero.georgina.models._
import java.security.MessageDigest
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object Utils {
  def transactionHash(t: Transaction): String = {
    val hasher = Hashing.sha256
    val format = DateTimeFormat.forPattern("yyyy-MM-dd");
    val str = Seq(t.date.toString(format),
        t.species,
        t.amount.toString,
        t.description).mkString
    hasher.hashString(str).toString
  }

  def defaultDateParam: Map[String, String] = {
    val format = DateTimeFormat.forPattern("yyyy-MM-dd");

    val thirtyDaysAgo: DateTime = new DateTime().minusDays(30);

    val today: DateTime = new DateTime();

    Map(
      "startDate" -> thirtyDaysAgo.toString(format),
      "endDate" -> today.toString(format)
    )
  }
}
