package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Dealer(name: String = "Dealer") extends Person {
  private var cardDeck : ListBuffer[Card] = ListBuffer.empty[Card]

  def generateDealerCards: ListBuffer[Card] = {
    cardDeck.clear()
    for {
      i <- 0 to 5
    } cardDeck ++= CardDeck().cardDeck
    cardDeck
  }

  def drawCard(): Card = {
    val rand = new scala.util.Random()
    val randInt : Int = rand.nextInt(cardDeck.size)
    val tmp = cardDeck(randInt)
    cardDeck.remove(randInt)
    tmp
  }

  override def toString: String = name
}
