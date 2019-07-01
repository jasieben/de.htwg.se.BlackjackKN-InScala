package de.htwg.se.blackjackKN.model.cardsComponent


trait CardDeckInterface {
  val cardDeck: List[CardInterface]

  def generateNumberCards: List[CardInterface]
  def generateFaceCards: List[CardInterface]
}
