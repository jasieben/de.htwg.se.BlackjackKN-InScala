package de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.gamelogic.model.{Ranks, Suits}

object CardFactory {
  def createFromString(string: String): CardInterface = {
    assert(string.trim.length >= 2 && string.trim.length <= 3)

    var suit = Suits.Hearts
    var rankInt: Int = -1
    var rankValue: Ranks.Value = Ranks.Ace

    // Edge case 10
    if (string.trim.length == 3) {
      rankInt = 10
    } else {
      string.trim.charAt(0) match {
        case 'A' =>
          rankValue = Ranks.Ace
        case 'J' =>
          rankValue = Ranks.Jack
        case 'K' =>
          rankValue = Ranks.King
        case 'Q' =>
          rankValue = Ranks.Queen
        case _ =>
          rankInt = string.charAt(0).asDigit
      }
    }

    string.trim.charAt(string.trim.length - 1) match {
      case 'H' =>
        suit = Suits.Hearts
      case 'S' =>
        suit = Suits.Spades
      case 'D' =>
        suit = Suits.Diamonds
      case 'C' =>
        suit = Suits.Clubs
    }

    if (rankInt == -1) {
      FaceCard(suit, rankValue)
    } else {
      NumberCard(suit, rankInt)
    }
  }
}
