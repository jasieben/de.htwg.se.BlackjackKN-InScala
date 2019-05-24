package de.htwg.se.blackjackKN.model

trait Card {
  def suit : Suits.Value
  def rank: Any
  var value : Int
  override def toString: String = {rank + " of " + suit}

  val suits: List[Suits.Value] = List(Suits.Hearts, Suits.Clubs, Suits.Diamonds, Suits.Spades)
}