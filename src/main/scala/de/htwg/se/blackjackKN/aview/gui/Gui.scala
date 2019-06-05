package de.htwg.se.blackjackKN.aview.gui

import scalafx.Includes._
import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.util.Observer
import scalafx.geometry._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.{Scene, SubScene}
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.{Text, TextAlignment}
import scalafx.scene.control._
import scalafx.scene.shape._

class Gui(controller: Controller) extends JFXApp with Observer{
  controller.add(this)

  val menuText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = "Hello " + controller.player.name + "!\nYour balance is " + controller.player.balance + "$"
  }

  val backgroundCard: Color = LightGrey
  object Cards {
    val stackCards = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = (stage.width - stage.width / 8).toInt
      y = (stage.height / 10).toInt
    }
    val dealerCard1 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = (stage.width / 2 - width + width/4).toInt
      y = (stage.height / 10).toInt
    }
    val dealerCard2 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = ((stage.width / 2) - width /4).toInt
      y = (stage.height / 10).toInt
    }
    val dealerCard3 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = ((stage.width / 2) - width /4 + width / 2).toInt
      y = (stage.height / 10).toInt
    }
    val dealerCard4 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = ((stage.width / 2) - width /4 + width).toInt
      y = (stage.height / 10).toInt
    }

    val dealerCard5 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = ((stage.width / 2) - width /4 + width * 1.5).toInt
      y = (stage.height / 10).toInt
    }
    val dealerCard6 = new Rectangle {
      fill = backgroundCard
      stroke = Black
      width = 100
      height = 150
      x = ((stage.width / 2) - width /4 + width * 2).toInt
      y = (stage.height / 10).toInt
    }

  }

  stage = new PrimaryStage {
    onCloseRequest = handle { exit() }
    title = "BlackjackKN"
    width = 1200
    height = 800
    scene = setMenuScene()
  }
  def startNewRound() : Unit = {
    val dialog = new TextInputDialog(defaultValue = (controller.player.balance / 8).toInt.toString) {
      initOwner(stage)
      title = "Bet amount"
      headerText = "What amount of your " + controller.player.balance + "$ would you like to bet, " + controller.player.name +"?"
      contentText = "Enter the amount here:"
    }

    val result = dialog.showAndWait()

    result match {
      case Some(value) =>
        var int : Double = 0
        try {
          int = value.toDouble
        } catch {
          case _: Throwable =>
            new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Bet failed"
              headerText = "Please enter a number and nothing else"
              contentText = "I mean, come on what's wrong with you?"
            }.showAndWait()
            return
        }
          if (controller.setBet(int.toInt)) {
            controller.startNewRound()
            setPlayingScene()
          } else {
            new Alert(AlertType.Error) {
              initOwner(stage)
              title = "Bet failed"
              headerText = "Not enough money"
              contentText = "You don't have enough money mate"
            }.showAndWait()
          }
      case None =>
    }


  }

  def exit() : Unit = {
    println("Exiting game...")
    System.exit(0)
  }

  def setPlayingScene() : Unit = {
    stage.scene = new Scene {
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(DarkGreen, SeaGreen))

        root = new Pane {
          children = List(Cards.dealerCard1, Cards.dealerCard2,
            Cards.dealerCard3,Cards.dealerCard4, Cards.dealerCard5, Cards.dealerCard6,
            Cards.stackCards)
        }
      }
  }

  def setMenuScene() : Scene = {
    new Scene {
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(PaleGreen, SeaGreen))
      root = new BorderPane {
        top = new HBox {
          children = new Text {
            text = "BLACKJACK"
            style = "-fx-font-size: 36pt"
            alignment = Pos.Center
          }
        }
        center = new VBox {

          alignment = Pos.Center
          spacing = 20
          style = "-fx-font-size: 12pt"
          children = Seq(
            menuText,
            new Button {
              text = "Start New Round!"
              onAction = handle {
                startNewRound()
              }
            },
            new Button {
              text = "Change Player Name"
              onAction = handle {
                changePlayer()
              }
            },
            new Button {
              text = "Exit Game"
              onAction = handle { exit() }
            }
          )
        }
        bottom = new HBox{
          alignment = Pos.Center
          spacing = 20
          children = Seq(
            new Text {
              text = "Developed by Jana Siebenhaller and Benjamin Jasper"
              padding = Insets(40,0,20,0)
            }
          )
        }
      }
    }
  }

  def changePlayer() : Unit = {
    val dialog = new TextInputDialog(controller.player.name) {
      initOwner(stage)
      title = "Change your name"
      headerText = "What name would you like to have?"
      contentText = "Enter the name here:"
    }

    val result = dialog.showAndWait()

    result match {
      case Some(value) =>
        controller.createNewPlayer(value)
        menuText.text = "Hello " + controller.player.name + "!\nYour balance is " + controller.player.balance + "$"
      case None =>
    }
  }

  override def update: Boolean = true
}
