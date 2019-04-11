package de.htwg.se.blackjackKN.model

class DealerSpec {

  import org.scalatest._
  import org.junit.runner.RunWith
  import org.scalatest.junit.JUnitRunner

  @RunWith(classOf[JUnitRunner])
  class DealerSpec extends WordSpec with Matchers {
    "A Dealer" should {
       val dealer = Dealer("Dealer", 1000000)
      "have a name" in {
        dealer.name should be ("Dealer")
      }
      "have a balance" in {
        dealer.balance should be (1000000)
      }
    }
  }
}
