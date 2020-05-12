package de.htwg.se.blackjackKN.model.personsComponent

import de.htwg.se.blackjackKN.model.Ranks
import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface

import scala.collection.mutable.ListBuffer

trait PersonsInterface {
  val name : String

  def getCard(index: Int): CardInterface

  def getHandSize: Int

  def getHandValue: Int

  def getLastHandCard: CardInterface

  def containsCardType(rank: Ranks.Value): Int
}
