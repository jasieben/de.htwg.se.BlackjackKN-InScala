package de.htwg.se.blackjackKN.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PersonSpec extends WordSpec with Matchers {
  "A Person" should {
    "have a name" in {
      Person("Player1", 1000).name should be ("Player1")
    }
    "have a balance" in {
      Person("Player1", 1000).balance should be (1000)
    }
  }




}
