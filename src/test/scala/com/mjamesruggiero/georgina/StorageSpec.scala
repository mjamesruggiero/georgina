package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import scalikejdbc._
import SQLInterpolation._

class StorageSpec extends ScalatraSuite with FunSuite {
  test("Storage should build a database table") {
    val returned = Storage.init
    returned.map(_("FIRST_NAME")) should equal(List("Alice", "Ted", "Bob"))
    returned.map(_("LAST_NAME")) should equal(List("Jones", "Smith", "Huang"))
    returned.map(_("EMAIL")) should equal(List("alice@example.com", "ted@example.com", "bob@example.com"))
    returned.map(_("PASSWORD")) should equal(List("passwORD", "p@ssw4rd", "P@ZZwerD"))
  }
}
