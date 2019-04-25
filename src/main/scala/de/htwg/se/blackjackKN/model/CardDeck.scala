package de.htwg.se.blackjackKN.model

case class CardDeck(countOfDecks : Int = 1) {
  private val faceCards : List[FaceCard] = FaceCard().getCards
  private val numberCards : List [NumberCard] = NumberCard().getCards
  val cardDeck: List[Card] = faceCards ::: numberCards

  override def toString: String = "CardDeck has " + cardDeck.size + " cards"
}
