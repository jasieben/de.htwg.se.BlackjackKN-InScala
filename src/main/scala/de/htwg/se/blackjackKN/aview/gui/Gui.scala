package de.htwg.se.blackjackKN.aview.gui

import scalafx.Includes._
import de.htwg.se.blackjackKN.controller.{Controller, GameState}
import de.htwg.se.blackjackKN.util.Observer
import javafx.scene.paint.ImagePattern
import scalafx.geometry._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.{Scene, SubScene}
import scalafx.scene.layout.{Background, BorderPane, FlowPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.{Text, TextAlignment}
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.shape._

class Gui(controller: Controller) extends JFXApp with Observer {
  controller.add(this)

  val backSideImagePattern = new ImagePattern(new Image("de/htwg/se/blackjackKN/res/red_back.png"))
  var gamestatesPointer : Int = 0

  val menuText: Text = new Text {
    textAlignment = TextAlignment.Center
    text = "Hello " + controller.player.name + "!\nYour balance is " + controller.player.balance + "$"
  }

  val balanceText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = "Balance: " + controller.player.balance + "$"
    fill = Black
  }
  val currentBetText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = "Current Bet: " + controller.player.bet.value + "$"
    fill = Black
  }

  object Cards {
    val stackCards: Card = new Card(stage) {
      x = (stage.width - stage.width / 8).toInt
    }
    val dealerCard1: Card = new Card(stage) {
      x = (stage.width / 2 - width + width / 4).toInt
    }
    val dealerCard2: Card = new Card(stage) {
      x = ((stage.width / 2) - width / 4).toInt
    }
    val dealerCard3: Card = new Card(stage) {
      x = ((stage.width / 2) - width / 4 + width / 2).toInt
    }
    val dealerCard4: Card = new Card(stage) {
      x = ((stage.width / 2) - width / 4 + width).toInt
    }

    val dealerCard5: Card = new Card(stage) {
      x = ((stage.width / 2) - width / 4 + width * 1.5).toInt
    }
    val dealerCard6: Card = new Card(stage) {
      x = ((stage.width / 2) - width / 4 + width * 2).toInt
    }
  }

  object Controls {
    val hitButton: Button = new Button {
      text = "Hit"
    }
    val standButton: Button = new Button {
      text = "Stand"
    }
  }

  stage = new PrimaryStage {
    onCloseRequest = handle {
      exit()
    }
    title = "BlackjackKN"
    width = 1400
    height = 800
    scene = setMenuScene()
  }

  def startNewRound(): Unit = {
    val dialog = new TextInputDialog(defaultValue = (controller.player.balance / 8).toInt.toString) {
      initOwner(stage)
      title = "Bet amount"
      headerText = "What amount of your " + controller.player.balance + "$ would you like to bet, " + controller.player.name + "?"
      contentText = "Enter the amount here:"
    }

    val result = dialog.showAndWait()

    result match {
      case Some(value) =>
        var int: Double = 0
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
          setPlayingScene()
          controller.startNewRound()
        } else {
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Bet failed"
            headerText = "Not enough money"
            contentText = "You don't have enough money mate"
          }.showAndWait()
          return
        }
      case None =>
        return
    }

  }

  def exit(): Unit = {
    println("Exiting game...")
    System.exit(0)
  }

  def setPlayingScene(): Unit = {
    stage.scene = new Scene {
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(DarkGreen, SeaGreen))
      root = new BorderPane {

        bottom = new VBox {
          alignment = Pos.Center
          spacing = 20
          style = "-fx-font-size: 14pt"
          margin = Insets(20,0,30,0)
          children = List(
            balanceText,
            currentBetText,
            new HBox {
              alignment = Pos.Center
              spacing = 40
              children = List(Controls.hitButton,
                Controls.standButton)
            }
          )
        }
        center = new Pane {
          margin = Insets(60,0,0,0)
          children = List(Cards.dealerCard1, Cards.dealerCard2,
            Cards.dealerCard3, Cards.dealerCard4, Cards.dealerCard5, Cards.dealerCard6,
            Cards.stackCards)
        }
      }
    }
  }

  def setMenuScene(): Scene = {
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
              onAction = handle {
                exit()
              }
            }
          )
        }
        bottom = new HBox {
          alignment = Pos.Center
          spacing = 20
          children = Seq(
            new Text {
              text = "Developed by Jana Siebenhaller and Benjamin Jasper"
              padding = Insets(40, 0, 20, 0)
            }
          )
        }
      }
    }
  }

  def changePlayer(): Unit = {
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

  override def update: Boolean = {
    var counter : Int = 0
    for (i <- gamestatesPointer until controller.gameStates.length) {
      controller.gameStates(i) match {
        case GameState.IDLE =>

        case GameState.SHUFFLING =>

        case GameState.FIRST_ROUND =>
          currentBetText.text = "Current Bet: " + controller.player.bet.value + "$"
          Cards.stackCards.setFill(backSideImagePattern)
          Cards.dealerCard1.setFill(controller.dealer.getCard(0).getBackgroundImagePattern)
          Cards.dealerCard2.setFill(backSideImagePattern)
        case GameState.STAND =>

        case GameState.HIT =>

        case GameState.REVEAL =>

        case GameState.DEALER_DRAWS =>

        case GameState.PLAYER_BUST =>

        case GameState.DEALER_BUST =>

        case GameState.PLAYER_BLACKJACK =>

        case GameState.WAITING_FOR_INPUT =>

        case GameState.PLAYER_WINS =>

        case GameState.PLAYER_LOOSE =>

        case GameState.PUSH =>

        case GameState.ACE =>

        case GameState.BET_FAILED =>

        case GameState.BET_SET =>

        case GameState.UNDO =>

        case GameState.REDO =>

        case GameState.NEW_NAME =>

        case GameState.DEALER_BLACKJACK =>

      }
      counter = i
    }
    gamestatesPointer = counter + 1
    true
  }
}
