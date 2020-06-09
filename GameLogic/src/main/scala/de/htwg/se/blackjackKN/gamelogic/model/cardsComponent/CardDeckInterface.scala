package de.htwg.se.blackjackKN.gamelogic.model.cardsComponent

trait CardDeckInterface extends Serializable {
  val cardDeck: List[CardInterface]

  def generateNumberCards: List[CardInterface]
  def generateFaceCards: List[CardInterface]
}
