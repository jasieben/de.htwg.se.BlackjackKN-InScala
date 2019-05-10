package de.htwg.se.blackjackKN.model

trait Card {
  def suit : String
  def rank: Any
  def value : Int
  def getCards: List[Any]
  override def toString: String = {rank + " of " + suit}
}
