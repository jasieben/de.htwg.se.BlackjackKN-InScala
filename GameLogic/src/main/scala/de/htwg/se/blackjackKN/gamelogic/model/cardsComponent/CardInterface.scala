package de.htwg.se.blackjackKN.gamelogic.model.cardsComponent

import de.htwg.se.blackjackKN.gamelogic.model.Suits

trait CardInterface {
  def suit : Suits.Value
  def rank: Any
  val value : Int
  def suits: List[Suits.Value]
  def getBackgroundImageFileName : String
  def toString: String
}
