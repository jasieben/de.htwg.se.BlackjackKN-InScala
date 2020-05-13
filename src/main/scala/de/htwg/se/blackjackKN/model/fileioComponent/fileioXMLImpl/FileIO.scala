package de.htwg.se.blackjackKN.model.fileioComponent.fileioXMLImpl

import com.google.inject.{Guice, Injector}
import com.google.inject.name.Names
import de.htwg.se.blackjackKN.BlackjackModule
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.blackjackKN.model.fileioComponent.FileIOInterface
import de.htwg.se.blackjackKN.model.personsComponent.Player

import scala.io.Source
import scala.util.Try
import scala.xml.{Elem, NodeSeq, PrettyPrinter}

class FileIO extends FileIOInterface {

  def load(playerName: String): Option[Player] = {
    val fileOption = Try(scala.xml.XML.loadFile(playerName + ".xml")).toOption
    val file = fileOption.getOrElse(
      return None
    )
    val balance: Int = (file \\ "player" \ "@balance").text.toInt
    val player = Player()
    Option(player.copy(name = playerName, balance = balance))
  }

  override def store(player: Player): Boolean = {
    storeString(player)
    true
  }

  def storeString(player: Player): Unit = {
    import java.io._
    val pw = new PrintWriter(new File(player.name + ".xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(playerToXml(player, player.balance))
    pw.write(xml)
    pw.close()
  }

  def playerToXml(player: Player, balance: Int): Elem = {
    <player balance={balance.toString}></player>
  }
}
