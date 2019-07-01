package de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl

import de.htwg.se.blackjackKN.model.cardsComponent.CardDeckInterface

case class CardDeck(countOfDecks : Int = 1) extends CardDeckInterface{
  private val faceCards : List[FaceCard] = generateFaceCards
  private val numberCards : List [NumberCard] = generateNumberCards
  val cardDeck: List[Card] = faceCards ::: numberCards

  override def toString: String = "CardDeck has " + cardDeck.size + " cards"
  def generateNumberCards: List[NumberCard] = {
    val cards: List[NumberCard] = for {
      n <- NumberCard().ranks
      s <- NumberCard().suits
    } yield NumberCard(s, n)
    cards
  }
  def generateFaceCards: List[FaceCard] = {
    val cards: List[FaceCard] = for {
      r <- FaceCard().ranks
      s <- FaceCard().suits
    } yield FaceCard(s, r._1)
    cards
  }
}
