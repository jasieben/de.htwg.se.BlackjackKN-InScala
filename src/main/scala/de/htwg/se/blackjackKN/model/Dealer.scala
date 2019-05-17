package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class Dealer(name: String = "Dealer") extends Person {
  private var cardDeck : ListBuffer[Card] = ListBuffer.empty[Card]

  def generateDealerCards: ListBuffer[Card] = {
    cardDeck.clear()
    for {
      i <- 0 to 5
    } cardDeck ++= CardDeck().cardDeck
    cardDeck = Random.shuffle(cardDeck)
    cardDeck
  }

  def drawCard(): Card = {
    val tmp = cardDeck.head
    cardDeck.remove(0)
    tmp
  }

  def renewCardDeck(): Unit = {
    cardDeck.clear()
    generateDealerCards
  }

  def getCardDeckSize : Int = cardDeck.size

  override def toString: String = name
}
