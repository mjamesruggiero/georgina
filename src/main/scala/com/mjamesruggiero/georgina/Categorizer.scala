package com.mjamesruggiero.georgina

case class Category(c: String)

class Categorizer(raw: String) {
  def categorize = {
    new CategorizationStrategy(raw).result  
  }
}

class CategorizationStrategy(s: String) {
  def result: Category = s match {
    case (desc) if desc.contains("NATURAL GR") => Category("grocery")
    case (desc) if desc.contains("NOB HILL") => Category("grocery")
    case (desc) if desc.contains("CHECK #") => Category("check")
    case (desc) if desc.contains("ATM") => Category("atm")
    case (desc) if desc.contains("AMAZON") => Category("amazon")
    case (desc) if desc.contains("POWER") => Category("utilities")
    case _ => Category("unknown")
  }
}
