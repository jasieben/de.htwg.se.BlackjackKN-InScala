package de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl

import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.{Ranks, Suits}

abstract class Card extends CardInterface{
  val suit : Suits.Value
  val rank: Any
  var value : Int
  override def toString: String = {rank + " of " + suit}

  val suits: List[Suits.Value] = List(Suits.Hearts, Suits.Clubs, Suits.Diamonds, Suits.Spades)

  def getBackgroundImageFileName : String = {
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
    fileName += ".png"
    fileName
  }
}
