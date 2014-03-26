package com.mjamesruggiero.georgina

import com.mjamesruggiero.georgina._
import org.scalatra.test.scalatest._
import org.scalatest.FunSuite
import scalikejdbc._
import SQLInterpolation._

class StorageSpec extends ScalatraSuite with FunSuite {
  test("Storage should build a database table") {
    val returned = Storage.init
    val l = returned.map(_("NAME"))
    l should equal(List("Alice", "Ted", "Bob"))
  }
}
