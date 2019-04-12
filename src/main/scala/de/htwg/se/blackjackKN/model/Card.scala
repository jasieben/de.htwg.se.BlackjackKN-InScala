package de.htwg.se.blackjackKN.model

case class Card(suit : String, value: Int, rank : String) {
  override def toString:String = rank + " of " + suit

}
