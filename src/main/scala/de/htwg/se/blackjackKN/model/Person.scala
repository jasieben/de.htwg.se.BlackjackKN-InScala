package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

trait Person {
  def name: String
  private var hand : ListBuffer[Card] = ListBuffer.empty[Card]

  def addCardToHand(card: Card): Card = {
    hand += card
    card
  }

  def clearHand() : Unit = {
    hand.clear()
  }

  def getCard(index : Int): Card = {
    hand(index)
  }

  def getHandSize : Int = {
    hand.size
  }

  def getHandValue : Int = {
    var v : Int = 0
    for {
      i <- hand.indices
    } v += hand(i).value
    v
  }

  def getLastHandCard : Card = {
    hand.last
  }
}
