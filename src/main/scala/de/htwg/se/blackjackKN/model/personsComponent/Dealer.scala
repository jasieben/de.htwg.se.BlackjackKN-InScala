package de.htwg.se.blackjackKN.model.personsComponent

import de.htwg.se.blackjackKN.model.Ranks
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.CardDeck

import scala.util.Random

case class Dealer(name: String = "Dealer", hand: List[CardInterface] = List[CardInterface](), cardDeck: List[CardInterface] = List[CardInterface]()) extends PersonsInterface {

  def generateDealerCards: Dealer = {
    val baseCardDeck : List[CardInterface] = CardDeck().cardDeck

    val playCards: List[CardInterface] = baseCardDeck ::: baseCardDeck ::: baseCardDeck ::: baseCardDeck ::: baseCardDeck

    copy(cardDeck = Random.shuffle(playCards))
  }

  def clearHand(): Dealer = {
    copy(hand = List[CardInterface]())
  }

  def drawCard(): CardInterface = {
    cardDeck.head
  }

  def dropCard(): Dealer = {
    copy(cardDeck = cardDeck.drop(1))
  }

  def renewCardDeck(): Dealer = {
    generateDealerCards
  }

  def getCardDeckSize: Int = cardDeck.size

  override def toString: String = name

  def addCardToHand(card: CardInterface): Dealer = {
    copy(hand = hand :+ card)
  }

  def getCard(index: Int): CardInterface = {
    hand(index)
  }

  def getHandSize: Int = {
    hand.size
  }

  def getHandValue: Int = {
    var v: Int = 0
    for {
      i <- hand.indices
    } v += hand(i).value
    v
  }

  def getLastHandCard: CardInterface = {
    hand.last
  }

  def containsCardType(rank: Ranks.Value): Int = {
    for (i <- hand.indices) {
      if (hand(i).rank == rank) {
        return i
      }
    }
    -1
  }
}
