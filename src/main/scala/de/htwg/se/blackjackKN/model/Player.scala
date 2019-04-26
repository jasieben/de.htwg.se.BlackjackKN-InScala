package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Player(name: String, balance: Int = 0) extends Person{

  private var playerHand : ListBuffer[Card] = ListBuffer.empty[Card]

  def addCardToHand(card: Card): Card = {
    playerHand += card
    card
  }

  def clearHand() : Unit = {
    playerHand.clear()
  }

  def getCard(index : Int): Card = {
    playerHand(index)
  }

  def getHandSize : Int = {
    playerHand.size
  }

  def getHandValue : Int = {
    var v : Int = 0
    for {
      i <- playerHand.indices
    } v += playerHand(i).value
    v
  }
  override def toString:String = name

}