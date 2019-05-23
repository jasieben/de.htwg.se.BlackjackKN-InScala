package de.htwg.se.blackjackKN.model

trait Person {

  def addCardToHand(card: Card): Card

  def clearHand()

  def getCard(index: Int): Card

  def getHandSize: Int

  def getHandValue: Int

  def getLastHandCard: Card

  def containsCardType(rank: Ranks.Value): Int
}
