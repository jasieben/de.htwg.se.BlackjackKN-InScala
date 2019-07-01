package de.htwg.se.blackjackKN.model.fileioComponent.fileioJSONImpl

import de.htwg.se.blackjackKN.model.fileioComponent.FileIOInterface
import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface
import play.api.libs.json.{JsNumber, JsValue, Json}
import java.io._

import com.google.inject.Guice
import de.htwg.se.blackjackKN.BlackjackModule

import scala.io.Source


class FileIO extends FileIOInterface{
  def load(playerName : String) : PlayerInterface = {

    val source: String = Source.fromFile(playerName + ".json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val injector = Guice.createInjector(new BlackjackModule)
    val player = injector.getInstance(classOf[PlayerInterface])
    val jsonBalance : Double = (json \ "balance").as[Double]
    player.setBalance(jsonBalance)
    player.setName(playerName)
    player
  }

  def store(player : PlayerInterface) : Boolean = {
    val jsonObj = Json.obj(
      "name" -> player.getName,
        "balance" -> JsNumber(player.balance)
    )
    val pw = new PrintWriter(new File(player.getName +  ".json"))
    pw.write(Json.prettyPrint(jsonObj))
    pw.close()

    true
  }
}
