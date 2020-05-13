package de.htwg.se.blackjackKN.model.personsComponent

import de.htwg.se.blackjackKN.model.Ranks
import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface

case class Player(
                   name: String = "Test",
                   hand: List[CardInterface] = List[CardInterface](),
                   balance: Int = 1000, bet: Option[Bet] = Option(Bet(0))) extends PersonsInterface {
  override def toString: String = name

  def addCardToHand(card: CardInterface): Player = {
    copy(hand = hand :+ card)
  }

  def newBet(value: Int): Player = {
    if (value <= balance) {
      copy(balance = balance - value, bet = Option(Bet(value)))
    } else {
      copy(bet = None)
    }
  }

  def replaceCardInHand(index: Int, newCard: CardInterface): Player = copy(hand = hand.updated(index, newCard))

  def changeBalance(value: Int): Player = copy(balance = value)

  def clearHand(): Player = copy(hand = List[CardInterface]())

  def getCard(index: Int): CardInterface = hand(index)

  def getHandSize: Int = hand.size

  def getHandValue: Int = {
    var v: Int = 0
    for {
      i <- hand.indices
    } v += hand(i).value
    v
  }

  def getLastHandCard: CardInterface = hand.last

  def containsCardType(rank: Ranks.Value): Int = {
    for (i <- hand.indices) {
      if (hand(i).rank == rank) {
        return i
      }
    }
    -1
  }
}
