package de.htwg.se.blackjackKN.model

case class CardDeck() {
  private val faceCard: List[((String, Int), String)] = FaceCard().faceCards
}
