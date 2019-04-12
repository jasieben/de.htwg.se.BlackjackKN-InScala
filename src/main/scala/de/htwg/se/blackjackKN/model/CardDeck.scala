package de.htwg.se.blackjackKN.model

case class CardDeck() {
  private val faceCards: List[((String, Int), String)] = FaceCard().faceCards
  private val numberCards = NumberCard().numcards
  val cardDeck: List[(Any, String)] = faceCards ::: numberCards
}
