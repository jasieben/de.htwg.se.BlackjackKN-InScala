package de.htwg.se.blackjackKN.model

import org.scalatest._
  import org.junit.runner.RunWith
  import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DealerSpec extends WordSpec with Matchers {
  "A Dealer" when {
    "new" should {
      val dealer = Dealer("Dealer")
      "have a name" in {
        dealer.name should be("Dealer")
      }
      "have a nice String representation" in {
        dealer.toString should be("Dealer")
      }
    }
  }
}