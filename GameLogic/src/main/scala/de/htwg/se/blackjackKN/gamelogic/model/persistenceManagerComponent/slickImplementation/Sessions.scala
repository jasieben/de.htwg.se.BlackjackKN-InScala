package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.slickImplementation

import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.CardFactory
import slick.ast.BaseTypedType
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape

class Sessions(tag: Tag) extends Table[GameManager](tag, "sessions") {
  def id = column[String]("id", O.PrimaryKey, O.AutoInc, O.SqlType("BIGSERIAL"))

  def dealerHand = column[List[CardInterface]]("dealerhand")

  def playerHands = column[List[List[CardInterface]]]("playerhands")

  def cardDeck = column[List[CardInterface]]("carddeck")

  def gameStates = column[List[GameState.Value]]("gamestates")

  def revealed = column[Boolean]("revealed")

  def currentPlayerInRound = column[String]("currentplayerinroud")

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[GameManager] = (id.?, dealerHand, playerHands, cardDeck, gameStates, revealed, currentPlayerInRound).mapTo[GameManager]

  implicit val stringListCardMapper: JdbcType[List[CardInterface]] with BaseTypedType[List[CardInterface]] = MappedColumnType.base[List[CardInterface], String](
    list => list.mkString(","),
    stringList => if (stringList.nonEmpty) stringList.split(',').map(string => CardFactory.createFromString(string)).toList else List[CardInterface]()
  )

  implicit val stringListListCardMapper: JdbcType[List[List[CardInterface]]] with BaseTypedType[List[List[CardInterface]]] = MappedColumnType.base[List[List[CardInterface]], String](
    list => {
      var result = List[String]()
      for (cards <- list) {
        result = result :+ cards.mkString(",")
      }
      result.mkString(";")
    },
    stringList => if (stringList.nonEmpty) stringList.split(';').map(cards => if (cards.nonEmpty) cards.split(',').map(card => CardFactory.createFromString(card)).toList else List[CardInterface]()).toList else List[List[CardInterface]]()
  )

  implicit val listGameStateMapper: BaseTypedType[List[GameState.Value]] = MappedColumnType.base[List[GameState.Value], String](
    list => list.mkString(","),
    stringList => if (stringList.nonEmpty) stringList.split(',').map(string => GameState.withName(string)).toList else List[GameState.Value]()
  )
}



