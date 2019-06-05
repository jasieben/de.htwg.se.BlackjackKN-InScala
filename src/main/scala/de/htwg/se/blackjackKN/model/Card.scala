package de.htwg.se.blackjackKN.model

import javafx.scene.paint.ImagePattern
import scalafx.scene.image.Image

trait Card {
  def suit : Suits.Value
  def rank: Any
  var value : Int
  override def toString: String = {rank + " of " + suit}

  val suits: List[Suits.Value] = List(Suits.Hearts, Suits.Clubs, Suits.Diamonds, Suits.Spades)

  def getBackgroundImagePattern : ImagePattern = {
    var fileName : String = ""
    rank match {
      case Ranks.Ace =>
        fileName += "A"
      case Ranks.Jack =>
        fileName += "J"
      case Ranks.King =>
        fileName += "K"
      case Ranks.Queen =>
        fileName += "Q"
      case _: Int =>
        fileName += rank.toString
    }
    suit match {
      case Suits.Hearts =>
        fileName += "H"
      case Suits.Spades =>
        fileName += "S"
      case Suits.Diamonds =>
        fileName += "D"
      case Suits.Clubs =>
        fileName += "C"
    }
    if (!(fileName.length == 2)) {
      println("Error in image parser with the card: " + this.toString)
    }
    fileName += ".png"
    new ImagePattern(new Image("de/htwg/se/blackjackKN/res/" + fileName))
  }
}