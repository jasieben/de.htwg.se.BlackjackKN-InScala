package de.htwg.se.blackjackKN.model

case class CardDeck() {
  private val faceCards : List[FaceCard] = FaceCard().getCards
  private val numberCards : List [NumberCard] = NumberCard().getCards
  val cardDeck: List[Card] = faceCards ::: numberCards
}
