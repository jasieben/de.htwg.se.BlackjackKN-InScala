package de.htwg.se.blackjackKN.aview.gui

import scalafx.Includes._
import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.util.Observer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{FlowPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent

class Gui(controller: Controller) extends JFXApp with Observer{
  controller.add(this)
  stage = new PrimaryStage {
    title = "BlackjackKN"
    width = 800
    height = 600
    scene = new Scene {
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(DarkGreen, SeaGreen))
      content = new HBox {
        children = Seq(
          new Text {
            text = "Hello " + controller.player.name
          },
          new Button {
            text = "Start New Round!"
            onAction = handle {
              startNewRound()
            }
          }
        )
      }
    }

  }
  def startNewRound() = {
    stage.scene = new Scene {
      fill = Black
      content = new FlowPane {
        children = Seq(
          new Text {
            text = "NEW ROUND"
            fill = White
          }
        )
      }
    }
  }

  override def update: Boolean = true
}
