package de.htwg.se.blackjackKN.model

trait Card {
  def suit : String
  def rank: Any
  def value : Int
  override def toString: String = {rank + " of " + suit}
}
