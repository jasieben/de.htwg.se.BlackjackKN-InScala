package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Dealer(name: String = "Dealer") extends Person {
  private var buffer : ListBuffer[Card] = ListBuffer.empty[Card]

  var dealerHand : ListBuffer[Card] = ListBuffer.empty[Card]

  def generateDealerCards: ListBuffer[Card] = {
    buffer.clear()
    for {
      i <- 0 to 5
    } buffer ++= CardDeck().cardDeck
    buffer
  }

  def drawCard(): Card = {
    val rand = new scala.util.Random()
    val randInt : Int = rand.nextInt(buffer.size)
    val tmp = buffer(randInt)
    buffer.remove(randInt)
    tmp
  }

  def getCard(index : Int): Card = {
    dealerHand(index)
  }

  def addCardToHand(card: Card): Card = {
    dealerHand += card
    card
  }

  def getHandSize: Int = dealerHand.size

  def clearHand() : Unit = dealerHand.clear()

  def getHandValue : Int = {
    var v : Int = 0
    for {
      i <- dealerHand.indices
    } v += dealerHand(i).value
    v
  }


  override def toString: String = name
}
