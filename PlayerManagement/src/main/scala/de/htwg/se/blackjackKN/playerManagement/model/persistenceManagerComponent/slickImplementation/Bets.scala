package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.slickImplementation

import de.htwg.se.blackjackKN.playerManagement.model.EndState.EndState
import de.htwg.se.blackjackKN.playerManagement.model.{Bet, EndState, Player}
import slick.ast.BaseTypedType
import slick.lifted.ProvenShape
import slick.jdbc.PostgresProfile.api._

class Bets (tag: Tag) extends Table[Bet](tag, "bets") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.SqlType("BIGSERIAL"))

  def value = column[Int]("value")

  def status = column[EndState]("balance")

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Bet] = (id.?, value, status).mapTo[Bet]

  implicit val listGameStateMapper: BaseTypedType[EndState] = MappedColumnType.base[EndState, String](
    endstate => endstate.toString,
    string => EndState.withName(string)
  )
}
