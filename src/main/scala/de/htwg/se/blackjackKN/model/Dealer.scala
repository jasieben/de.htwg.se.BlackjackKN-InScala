package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class Dealer(name: String = "Dealer", handList : ListBuffer[Card] = ListBuffer.empty[Card], deck: ListBuffer[Card] = ListBuffer.empty[Card]) extends Person {
  private var cardDeck : ListBuffer[Card] = deck
  var hand : ListBuffer[Card] = handList

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

  def copy() : Dealer = {
    Dealer(this.name, hand.clone(), cardDeck.clone())
  }

  override def toString: String = name

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

  def containsCardType(rank: Ranks.Value) : Int = {
    for (i <- hand.indices) {
      if (hand(i).rank == rank) {
        return i
      }
    }
    -1
  }
}
