package de.htwg.se.blackjackKN.model

import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.CardDeck

import scala.util.Random

case class Dealer(name: String = "Dealer", hand: List[CardInterface] = List[CardInterface](), cardDeck: List[CardInterface] = List[CardInterface]()) extends PersonsInterface {


}
