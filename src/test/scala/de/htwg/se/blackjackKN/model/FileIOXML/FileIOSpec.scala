package de.htwg.se.blackjackKN.model.FileIOXML

import de.htwg.se.blackjackKN.model.fileioComponent.fileioXMLImpl.FileIO
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import scalafx.stage.Modality.None

@RunWith(classOf[JUnitRunner])
class FileIOSpec extends WordSpec with Matchers {
  "An XML File IO" should {
    val fileIO = new FileIO()
    var player: Player = Player()
    "save and load the balance" in {
      player = player.copy(balance = 400)
      fileIO.store(player) should be (true)
      player = player.copy(balance = 1000)
      player = fileIO.load("Test").get
      player.balance should be (400)
    }
    "not crash if file exists" in {
      val playerOption = fileIO.load("Test1233121").isEmpty
      playerOption should be (true)
    }
  }
}
