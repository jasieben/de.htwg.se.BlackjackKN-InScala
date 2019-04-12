package de.htwg.se.blackjackKN.model

case class Dealer(name: String) extends Person {
  override def toString: String = name
}
