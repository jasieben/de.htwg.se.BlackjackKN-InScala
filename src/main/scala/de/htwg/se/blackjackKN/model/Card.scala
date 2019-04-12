package de.htwg.se.blackjackKN.model

trait Card {
  def suit : String
  def rank: String
  def value : Int
  def getCards: List[Any]
}
