package de.htwg.se.blackjackKN.aview.gui

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.stage._


class CardGraphic(stage: Stage) extends Rectangle{
  val backgroundCard: Color = LightGrey
  fill = Transparent
  width = 110
  height = 175
  //stroke = Black
}
