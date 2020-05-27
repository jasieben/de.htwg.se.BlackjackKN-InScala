package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.model.{Dealer, Ranks}
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.CardDeck

import scala.util.Random

case class GameManager(dealerHand: List[CardInterface], playerHands: List[List[CardInterface]], cardDeck: List[CardInterface]) {

  def generateDealerCards: GameManager = {
    val baseCardDeck: List[CardInterface] = CardDeck().cardDeck

    val playCards: List[CardInterface] = baseCardDeck ::: baseCardDeck ::: baseCardDeck ::: baseCardDeck ::: baseCardDeck

    copy(cardDeck = Random.shuffle(playCards))
  }

  def addCardToPlayerHand(playerIndex: Int, card: CardInterface): GameManager = {
    val playerHand: List[CardInterface] = playerHands(playerIndex) :+ card

    copy(playerHands = playerHands.updated(playerIndex, playerHand))
  }

  def replaceCardInHand(playerIndex: Int, handIndex: Int, newCard: CardInterface): GameManager = {
    val playerHand: List[CardInterface] = playerHands(playerIndex).updated(handIndex, newCard)
    copy(playerHands = playerHands.updated(playerIndex, playerHand))
  }

  def clearHand(playerIndex : Int): GameManager = {
    copy(playerHands = playerHands.updated(playerIndex, List[CardInterface] ()))
  }

  def getCard(playerIndex : Int, handIndex: Int): CardInterface = {
    playerHands(playerIndex)(handIndex)
  }

  def getHandSize(playerIndex: Int): Int = {
    playerHands(playerIndex).size
  }

  def getHandValue(playerIndex: Int): Int = {
    var v: Int = 0
    for {
      i <- playerHands(playerIndex).indices
    } v += playerHands(playerIndex)(i).value
    v
  }

  def getLastHandCard(playerIndex: Int): CardInterface = { playerHands(playerIndex).last }

  def containsCardType(playerIndex: Int, rank: Ranks.Value): Int = {
    val playerHand: List[CardInterface] = playerHands(playerIndex);
    for (i <- playerHand.indices) {
      if (playerHand(i).rank == rank) {
        return i
      }
    }
    -1
  }

  def clearDealerHand(): GameManager = {
    copy(dealerHand = List[CardInterface]())
  }

  def drawCard(): CardInterface = {
    cardDeck.head
  }

  def dropCard(): GameManager = {
    copy(cardDeck = cardDeck.drop(1))
  }

  def renewCardDeck(): GameManager = {
    generateDealerCards
  }

  def getCardDeckSize: Int = cardDeck.size

  def addCardToDealerHand(card: CardInterface): GameManager = {
    copy(dealerHand = dealerHand :+ card)
  }

  def getCard(index: Int): CardInterface = {
    dealerHand(index)
  }

  def getDealerHandSize: Int = {
    dealerHand.size
  }

  def getDealerHandValue: Int = {
    var v: Int = 0
    for {
      i <- dealerHand.indices
    } v += dealerHand(i).value
    v
  }

  def getLastDealerHandCard: CardInterface = {
    dealerHand.last
  }

  def containsCardType(rank: Ranks.Value): Int = {
    for (i <- dealerHand.indices) {
      if (dealerHand(i).rank == rank) {
        return i
      }
    }
    -1
  }
}
