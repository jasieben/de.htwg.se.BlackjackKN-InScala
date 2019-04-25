package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Dealer(name: String = "Dealer") extends Person {
  var availCards : List[Card] = CardDeck().cardDeck
  var buffer : ListBuffer[Card] = ListBuffer.empty[Card]

  def generateDealerCards: ListBuffer[Card] = {
    for {
      i <- 0 to 5
    } buffer ++= CardDeck().cardDeck
    buffer
  }

  def drawCard(): Card = {
    val rand = new scala.util.Random()
    var randInt : Int = rand.nextInt() % buffer.size
    if (randInt < 0)
      randInt = randInt * -1
    val tmp = buffer(randInt)
    buffer.remove(randInt)
    tmp
  }


  override def toString: String = name
}
