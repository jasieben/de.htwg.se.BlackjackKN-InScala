package de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl

import de.htwg.se.blackjackKN.model.Ranks
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.{Card, CardDeck}
import de.htwg.se.blackjackKN.model.personsComponent.DealerInterface

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class Dealer(name: String = "Dealer", handList : ListBuffer[CardInterface] = ListBuffer.empty[CardInterface], deck: ListBuffer[CardInterface] = ListBuffer.empty[CardInterface]) extends DealerInterface {
  private var cardDeck : ListBuffer[CardInterface] = deck
  var hand : ListBuffer[CardInterface] = handList

  def generateDealerCards: ListBuffer[CardInterface] = {
    cardDeck.clear()
    for {
      i <- 0 to 5
    } cardDeck ++= CardDeck().cardDeck
    cardDeck = Random.shuffle(cardDeck)
    cardDeck
  }

  def drawCard(): CardInterface = {
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

  def addCardToHand(card: CardInterface): CardInterface = {
    hand += card
    card
  }

  def clearHand() : Unit = {
    hand.clear()
  }

  def getCard(index : Int): CardInterface = {
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

  def getLastHandCard : CardInterface = {
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
