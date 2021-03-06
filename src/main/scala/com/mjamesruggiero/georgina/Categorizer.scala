package com.mjamesruggiero.georgina

case class Category(c: String)

class Categorizer(raw: String) {
  def categorize = {
    new StringContainsStrategy(raw).result
  }
}

trait CategorizationStrategy {
  def result: Category
}

class StringContainsStrategy(s: String) extends CategorizationStrategy {
  def result: Category = s match {
    case (desc) if desc.toLowerCase.contains("alameda alliance payroll") => Category("salary")
    case (desc) if desc.toLowerCase.contains("alameda gas") => Category("transportation")
    case (desc) if desc.toLowerCase.contains("alameda natural") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("alameda power") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("amazon") => Category("amazon")
    case (desc) if desc.toLowerCase.contains("atm withdrawal") => Category("atm")
    case (desc) if desc.toLowerCase.contains("aveda") => Category("household")
    case (desc) if desc.toLowerCase.contains("bill pay alameda county i") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("bill pay sbc") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("books inc") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("bounce") => Category("clothing")
    case (desc) if desc.toLowerCase.contains("ca dmv") => Category("transportation")
    case (desc) if desc.toLowerCase.contains("check #") => Category("check")
    case (desc) if desc.toLowerCase.contains("check deposit") => Category("asset")
    case (desc) if desc.toLowerCase.contains("cole hardware") => Category("household")
    case (desc) if desc.toLowerCase.contains("costplus") => Category("household")
    case (desc) if desc.toLowerCase.contains("credomobile") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("cvs") => Category("household")
    case (desc) if desc.toLowerCase.contains("destroy all software") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("east bay municip") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("edward jones") => Category("savings")
    case (desc) if desc.toLowerCase.contains("encinal market") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("engine works") => Category("transportation")
    case (desc) if desc.toLowerCase.contains("feel good bakery") => Category("dining")
    case (desc) if desc.toLowerCase.contains("github") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("hsbc online transfer") => Category("savings")
    case (desc) if desc.toLowerCase.contains("ikea") => Category("household")
    case (desc) if desc.toLowerCase.contains("interest payment") => Category("interest")
    case (desc) if desc.toLowerCase.contains("jazzercise") => Category("health and exercise")
    case (desc) if desc.toLowerCase.contains("lamorinda spanis") => Category("education")
    case (desc) if desc.toLowerCase.contains("love at first bite") => Category("dining")
    case (desc) if desc.toLowerCase.contains("natural gr") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("netflix.com") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("nob hill") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("nob hill") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("office max") => Category("household")
    case (desc) if desc.toLowerCase.contains("old navy") => Category("clothing")
    case (desc) if desc.toLowerCase.contains("pacific gas") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("paypal") => Category("paypal")
    case (desc) if desc.toLowerCase.contains("peet's") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("poppy red") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("power") => Category("utilities")
    case (desc) if desc.toLowerCase.contains("purchase - kohl") => Category("household")
    case (desc) if desc.toLowerCase.contains("railscasts") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("redbubble") => Category("clothes")
    case (desc) if desc.toLowerCase.contains("reilly media") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("restaurant") => Category("dining")
    case (desc) if desc.toLowerCase.contains("safeway") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("sharethrough inc direct") => Category("salary")
    case (desc) if desc.toLowerCase.contains("shell oil") => Category("transportation")
    case (desc) if desc.toLowerCase.contains("target") => Category("household")
    case (desc) if desc.toLowerCase.contains("the melt-embarcade") => Category("dining")
    case (desc) if desc.toLowerCase.contains("tj maxx") => Category("clothing")
    case (desc) if desc.toLowerCase.contains("to savings") => Category("savings")
    case (desc) if desc.toLowerCase.contains("tot tank") => Category("household")
    case (desc) if desc.toLowerCase.contains("toy safari") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("treasury direct") => Category("savings")
    case (desc) if desc.toLowerCase.contains("valero") => Category("transportation")
    case (desc) if desc.toLowerCase.contains("visa wells") => Category("credit card")
    case (desc) if desc.toLowerCase.contains("walgreen") => Category("household")
    case (desc) if desc.toLowerCase.contains("whole foods") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("wildflower cafe") => Category("dining")
    case (desc) if desc.toLowerCase.contains("withdrawal in branch") => Category("cash")
    case (desc) if desc.toLowerCase.contains("yoshi") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("etsy.com") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("dandelion flow") => Category("entertainment")
    case (desc) if desc.toLowerCase.contains("trader joe") => Category("grocery")
    case (desc) if desc.toLowerCase.contains("recurring transfer") => Category("savings")
    case (desc) if desc.toLowerCase.contains("anthropologie") => Category("entertainment")
    case _ => Category("unknown")
  }
}
