package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Dealer(name: String = "Dealer") extends Person {
  var availCards : List[Card] = CardDeck().cardDeck
  var buffer : ListBuffer[Card] = ListBuffer.empty[Card]


  def generateDealerCards: List[Card] = {
    for {
      i <- 1 to 5
    } availCards ++= CardDeck().cardDeck
    availCards.copyToBuffer(buffer)
    availCards
  }


  override def toString: String = name
}
