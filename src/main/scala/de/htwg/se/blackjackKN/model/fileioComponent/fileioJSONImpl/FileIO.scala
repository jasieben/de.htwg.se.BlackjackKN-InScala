package de.htwg.se.blackjackKN.model.fileioComponent.fileioJSONImpl

import de.htwg.se.blackjackKN.model.fileioComponent.FileIOInterface
import play.api.libs.json.{JsNumber, JsValue, Json}
import java.io._

import com.google.inject.Guice
import de.htwg.se.blackjackKN.BlackjackModule
import de.htwg.se.blackjackKN.model.personsComponent.Player

import scala.io.Source


class FileIO extends FileIOInterface{
  def load(playerName : String) : Player = {

    val source: String = Source.fromFile(playerName + ".json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val player = Player();
    val jsonBalance : Int = (json \ "balance").as[Int]
    player.copy(balance = jsonBalance, name = playerName)
  }

  def store(player : Player) : Boolean = {
    val jsonObj = Json.obj(
      "name" -> player.name,
        "balance" -> JsNumber(player.balance)
    )
    val pw = new PrintWriter(new File(player.name +  ".json"))
    pw.write(Json.prettyPrint(jsonObj))
    pw.close()

    true
  }
}
