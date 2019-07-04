package de.htwg.se.blackjackKN.model.FileIOXML

import de.htwg.se.blackjackKN.model.fileioComponent.fileioXMLImpl.FileIO
import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface
import de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class FileIOSpec extends WordSpec with Matchers {
  "An XML File IO" should {
    val fileIO = new FileIO()
    var player: PlayerInterface = Player()
    "save and load the balance" in {
      player.setBalance(400)
      fileIO.store(player) should be (true)
      player.setBalance(1000)
      player = fileIO.load("Test")
      player.balance should be (400)
    }
  }
}
