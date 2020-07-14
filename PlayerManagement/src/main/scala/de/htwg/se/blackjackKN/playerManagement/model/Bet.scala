package de.htwg.se.blackjackKN.playerManagement.model

import de.htwg.se.blackjackKN.playerManagement.model.EndState.EndState

case class Bet(id: Option[Int] = None, value: Int, status: EndState = EndState.ONGOING) {}
