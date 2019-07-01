package de.htwg.se.blackjackKN.model.cardsComponent

import de.htwg.se.blackjackKN.model.Suits

trait CardInterface {
  def suit : Suits.Value
  def rank: Any
  var value : Int
  def suits: List[Suits.Value]
  def getBackgroundImageFileName : String
}
