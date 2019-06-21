package de.htwg.se.blackjackKN.model.personsComponent

import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface

import scala.collection.mutable.ListBuffer

trait DealerInterface extends PersonsInterface {
  def generateDealerCards : ListBuffer[CardInterface]
  def getCardDeckSize : Int
  def renewCardDeck() : Unit
  def drawCard() : CardInterface
}
