package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.slickImplementation

import de.htwg.se.blackjackKN.playerManagement.model.{Bet, Player}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class Players(tag: Tag) extends Table[(Option[String], String, Int, Option[Int])](tag, "players") {
  val bets = TableQuery[Bets]

  def id = column[String]("id", O.PrimaryKey, O.AutoInc, O.SqlType("BIGSERIAL"))

  def name = column[String]("name")

  def balance = column[Int]("balance")

  def betId = column[Int]("betId")

  def betFK = foreignKey("betFK", betId, bets)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)

  // Every table needs a * projection with the same type as the table's type parameter
  def *  = (id.?, name, balance, betId.?)
}



