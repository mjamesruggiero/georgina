package com.mjamesruggiero.georgina

import java.security.MessageDigest
import com.mjamesruggiero.georgina.Transaction._
import org.joda.time.format.DateTimeFormat
import com.google.common.hash._

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
}
