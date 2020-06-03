package de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.{Ranks, Suits}
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface

abstract class Card extends CardInterface{
  val suit : Suits.Value
  val rank: Any
  val value : Int

  override def toString: String = {rank + " of " + suit}

  val suits: List[Suits.Value] = List(Suits.Hearts, Suits.Clubs, Suits.Diamonds, Suits.Spades)

  def getCardId: String = {
    var id = ""
    rank match {
      case Ranks.Ace =>
        id += "A"
      case Ranks.Jack =>
        id += "J"
      case Ranks.King =>
        id += "K"
      case Ranks.Queen =>
        id += "Q"
      case _: Int =>
        id += rank.toString
    }
    suit match {
      case Suits.Hearts =>
        id += "H"
      case Suits.Spades =>
        id += "S"
      case Suits.Diamonds =>
        id += "D"
      case Suits.Clubs =>
        id += "C"
    }
    id
  }

  def getBackgroundImageFileName : String = {
    s"$getCardId.png"
  }
}
