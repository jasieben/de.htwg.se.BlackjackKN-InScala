package de.htwg.se.blackjackKN.aview.gui

import scalafx.Includes._
import de.htwg.se.blackjackKN.controller.{Controller, GameState}
import de.htwg.se.blackjackKN.model._
import de.htwg.se.blackjackKN.util.Observer
import javafx.scene.paint.ImagePattern
import scalafx.animation._
import scalafx.geometry._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.{Scene, SubScene}
import scalafx.scene.layout.{AnchorPane, Background, BackgroundFill, BackgroundImage, BorderPane, ColumnConstraints, CornerRadii, FlowPane, GridPane, HBox, Pane, TilePane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.{Text, TextAlignment}
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.shape._
import scalafx.stage.Screen

import scala.language.postfixOps

class Gui(controller: Controller) extends JFXApp with Observer {
  controller.add(this)

  val backSideImagePattern = new ImagePattern(new Image("de/htwg/se/blackjackKN/res/blue_back.png"))
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

  val statusText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = ""
    fill = Black
  }

  val playerHandValueText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = ""
    fill = Black
  }

  val dealerHandValueText : Text = new Text {
    textAlignment = TextAlignment.Center
    text = ""
    fill = Black
  }

  object Cards {

    val stackCards: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard1: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard2: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard3: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard4: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard5: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val dealerCard6: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }

    val playerCard1: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val playerCard2: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val playerCard3: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val playerCard4: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val playerCard5: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }
    val playerCard6: CardGraphic = new CardGraphic(stage) {
      x = posStackCardx
    }

    val dealerCards : List[CardGraphic] = List(dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5, dealerCard6)
    val playerCards : List[CardGraphic] = List(playerCard1, playerCard2, playerCard3, playerCard4, playerCard5, playerCard6)
  }

  object Controls {
    val hitButton: Button = new Button {
      text = "Hit"
      onAction = handle {controller.hitCommand()}
    }
    val standButton: Button = new Button {
      text = "Stand"
      onAction = handle {controller.standCommand()}
    }
    val newRoundButton : Button = new Button {
      text = "Start new round"
      onAction = handle {startNewRound()}
      disable = true
    }
  }

  stage = new PrimaryStage {
    onCloseRequest = handle {
      exit()
    }
    val bounds: Rectangle2D = Screen.primary.visualBounds

    if (bounds.maxY >= 1080) {
      width = 1400
      height = 900
    } else {

      width = 1300
      height = 700
    }
    title = "BlackjackKN"
    scene = getMenuScene
  }

  def startNewRound(): Unit = {

    val dialog = new TextInputDialog(defaultValue = (controller.player.balance / 10).toInt.toString) {
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
        }
      case None =>
    }

  }

  def exit(): Unit = {
    println("Exiting game...")
    System.exit(0)
  }


  val posStackCardx: Int = (stage.width - stage.width / 8).toInt

  val posDealerCard1x : Int = (stage.width / 2 - Cards.dealerCard1.width + Cards.dealerCard1.width / 4).toInt
  val posDealerCard2x : Int = ((stage.width / 2) - Cards.dealerCard2.width / 4).toInt
  val posDealerCard3x : Int = ((stage.width / 2) - (Cards.stackCards.width / 4) + (Cards.stackCards.width / 2)).toInt
  val posDealerCard4x : Int = ((stage.width / 2) - Cards.stackCards.width / 4 + Cards.stackCards.width).toInt
  val posDealerCard5x : Int = ((stage.width / 2) - Cards.stackCards.width / 4 + Cards.stackCards.width * 1.5).toInt
  val posDealerCard6x : Int = ((stage.width / 2) - Cards.stackCards.width / 4 + Cards.stackCards.width * 2).toInt

  val posPlayerCard1x : Int = (stage.width / 2 - Cards.playerCard1.width / 2).toInt
  val posPlayerCard1y : Int = (stage.height / 2.5).toInt

  val posPlayerCard2x : Int = posPlayerCard1x + (Cards.playerCard1.width - Cards.playerCard1.width / 2).toInt
  val posPlayerCard2y : Int = posPlayerCard1y - (Cards.playerCard2.height / 6).toInt

  val posPlayerCard3x : Int = (posPlayerCard2x + Cards.stackCards.width.toInt) - Cards.stackCards.width.toInt / 2
  val posPlayerCard3y : Int = posPlayerCard2y - Cards.playerCard3.height.toInt / 6

  val posPlayerCard4x : Int = (posPlayerCard3x + Cards.stackCards.width.toInt) - Cards.stackCards.width.toInt / 2
  val posPlayerCard4y : Int = posPlayerCard3y - Cards.playerCard4.height.toInt / 6

  val posPlayerCard5x : Int = (posPlayerCard4x + Cards.stackCards.width.toInt) - Cards.stackCards.width.toInt / 2
  val posPlayerCard5y : Int = posPlayerCard4y - Cards.playerCard5.height.toInt / 6


  val posPlayerCard6x : Int = (posPlayerCard5x + Cards.stackCards.width.toInt) - Cards.stackCards.width.toInt / 2
  val posPlayerCard6y : Int = posPlayerCard5y - Cards.playerCard6.height.toInt / 6

  val posDealerCardsX : List[Int] = List(posDealerCard1x,posDealerCard2x,posDealerCard3x,
    posDealerCard4x,posDealerCard5x,posDealerCard6x)

  val posPlayerCardsX : List[Int] = List(posPlayerCard1x, posPlayerCard2x, posPlayerCard3x, posPlayerCard4x,
    posPlayerCard5x, posPlayerCard6x)

  val posPlayerCardsY : List[Int] = List(posPlayerCard1y, posPlayerCard2y, posPlayerCard3y, posPlayerCard4y,
    posPlayerCard5y, posPlayerCard6y)

  val timelineD1 = new Timeline {
    cycleCount = 1
    autoReverse = false
    keyFrames = Seq(
      at (1.0 s) {Cards.dealerCard1.x -> posDealerCard1x tween Interpolator.EaseBoth})

  }
  val timelineD2 = new Timeline {
    cycleCount = 1
    autoReverse = false
    keyFrames = Seq(
      at (1.0 s) {Cards.dealerCard2.x -> posDealerCard2x tween Interpolator.EaseBoth})

  }
  val timelineP1 = new Timeline {
    cycleCount = 1
    autoReverse = false
    keyFrames = Seq(
      at (1.0 s) {Cards.playerCard1.x -> posPlayerCard1x tween Interpolator.EaseBoth},
      at (1.0 s) {Cards.playerCard1.y -> posPlayerCard1y})
  }
  val timelineP2 = new Timeline {
    cycleCount = 1
    autoReverse = false
    keyFrames = Seq(
      // TOOO: playerCard1 coordinates need to be constant
      at (1.0 s) {Cards.playerCard2.x -> posPlayerCard2x tween Interpolator.EaseBoth},
      at (1.0 s) {Cards.playerCard2.y -> posPlayerCard2y tween Interpolator.EaseBoth})
  }

  def buildTimeline(card : CardGraphic, toX : Int, toY : Int) : Timeline = {
    new Timeline {
      cycleCount = 1
      autoReverse = false
      keyFrames = Seq(
        at (1.0 s) {card.x -> toX tween Interpolator.EaseBoth},
        at (1.0 s) {card.y -> toY tween Interpolator.EaseBoth})
    }
  }

  def setPlayingScene(): Unit = {
    for (c <- Cards.dealerCards) {
      c.setFill(Transparent)
      c.x = posStackCardx
    }
    for (c <- Cards.playerCards) {
      c.setFill(Transparent)
      c.x = posStackCardx
      c.y = Cards.stackCards.y.toInt
    }
    stage.scene = new Scene {

      root = new BorderPane {
        style = "-fx-background-color: linear-gradient(to bottom right, green, #0dad0a);"
        bottom = new GridPane {
          style = "-fx-font-size: 14pt"
          margin = Insets(20, 0, 30, 0)
          columnConstraints = Seq(new ColumnConstraints{
            halignment = HPos.Left
            percentWidth = 100/3
          },new ColumnConstraints{
            halignment = HPos.Center
            percentWidth = 100/3
          },new ColumnConstraints{
            halignment = HPos.Right
            percentWidth = 100/3
          })
          add(new VBox {
            alignment = Pos.CenterLeft
            margin = Insets(0,0,0,20)
            spacing = 20
            children = Seq(
              new Button {
                text = "Main menu"
                onAction = handle {
                  setMenuScene()
                }
              },
              playerHandValueText,
              dealerHandValueText,
              Controls.newRoundButton
            )
          },0,0)
          add(new VBox {
            alignment = Pos.Center
            spacing = 20
            children = Seq(
              balanceText
              ,
              currentBetText
              ,
              statusText
              ,
              new HBox {
                alignment = Pos.Center
                spacing = 40
                children = List(Controls.hitButton,
                  Controls.standButton)
              })
          },1,0)
          }
          center = new Pane {
            margin = Insets(60, 0, 0, 0)
            children = List(Cards.dealerCard1, Cards.dealerCard2,
              Cards.dealerCard3, Cards.dealerCard4, Cards.dealerCard5, Cards.dealerCard6,
              Cards.stackCards,
              Cards.playerCard1,
              Cards.playerCard2,
              Cards.playerCard3,
              Cards.playerCard4,
              Cards.playerCard5,
              Cards.playerCard6)
          }
        }
      }
    }

  def setMenuScene() : Unit = {
    menuText.text = "Hello " + controller.player.name + "!\nYour balance is " + controller.player.balance + "$"
    stage.scene = getMenuScene
  }

  def getMenuScene : Scene = {
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

  def getBackgroundImagePattern(card : Card): ImagePattern = {
    new ImagePattern(new Image("de/htwg/se/blackjackKN/res/" + card.getBackgroundImageFileName))
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
          Controls.standButton.setDisable(true)
          Controls.hitButton.setDisable(true)
          currentBetText.text = "Current Bet: " + controller.player.bet.value + "$"
          balanceText.text = "Balance: " + controller.player.balance + "$"
          Cards.stackCards.setFill(backSideImagePattern)
          Cards.dealerCard2.setFill(backSideImagePattern)
          Cards.playerCard1.setFill(backSideImagePattern)
          Cards.playerCard2.setFill(backSideImagePattern)

          Cards.dealerCard1.setFill(backSideImagePattern)
          timelineP1.play()
          timelineP1.onFinished = handle {
            Cards.playerCard1.setFill(getBackgroundImagePattern(controller.player.getCard(0)))
            timelineD1.play()
          }

          timelineD1.onFinished = handle {
            Cards.dealerCard1.setFill(getBackgroundImagePattern(controller.dealer.getCard(0)))
            timelineP2.play()
          }

          timelineP2.onFinished = handle {
            playerHandValueText.text = controller.player.name + "'s hand value: " + controller.player.getHandValue
            Cards.playerCard2.setFill(getBackgroundImagePattern(controller.player.getCard(1)))
            timelineD2.play()
          }
          timelineD2.onFinished = handle {
            dealerHandValueText.text = "Dealer's hand value: " + controller.dealer.getCard(0).value
          }
        case GameState.STAND =>
          statusText.text = controller.player.name + " stands"
        case GameState.HIT =>
          val c = Cards.playerCards(controller.player.getHandSize - 1)
          c.setFill(backSideImagePattern)
          val tl = buildTimeline(c, posPlayerCardsX(controller.player.getHandSize - 1), posPlayerCardsY(controller.player.getHandSize - 1))
          tl.play()
          tl.onFinished = handle {
            c.setFill(getBackgroundImagePattern(controller.player.getLastHandCard))
            playerHandValueText.text = controller.player.name + "'s hand value: " + controller.player.getHandValue
          }
        case GameState.REVEAL =>
          Cards.dealerCard2.setFill(getBackgroundImagePattern(controller.dealer.getCard(1)))
          dealerHandValueText.text = "Dealer's hand value: " + controller.dealer.getHandValue
        case GameState.DEALER_DRAWS =>
          for (i <- 2 until controller.dealer.getHandSize) {
            Cards.dealerCards(i).setFill(backSideImagePattern)
            val tl = buildTimeline(Cards.dealerCards(i), posDealerCardsX(i), Cards.dealerCards(i).y.toInt)
            tl.play()
            tl.onFinished = handle {
              Cards.dealerCards(i).setFill(getBackgroundImagePattern(controller.dealer.getCard(i)))
              dealerHandValueText.text = "Dealer's hand value: " + controller.dealer.getHandValue
            }
          }
        case GameState.PLAYER_BUST =>
          playerHandValueText.text = controller.player.name + " busts (value: "+ controller.player.getHandValue + ")"

        case GameState.DEALER_BUST =>
          dealerHandValueText.text = "Dealer busts (value: " + controller.dealer.getHandValue + ")"
        case GameState.PLAYER_BLACKJACK =>
          playerHandValueText.text = controller.player.name + "has a Blackjack (value: 21)"
        case GameState.WAITING_FOR_INPUT =>
          statusText.text = "Would you like to hit or stand?"
          Controls.standButton.setDisable(false)
          Controls.hitButton.setDisable(false)

        case GameState.PLAYER_WINS =>
          balanceText.text = "Balance: " + controller.player.balance  + "$"
          currentBetText.text = "Current bet: 0$"
          statusText.text = controller.player.name + " wins!"
          Controls.standButton.setDisable(true)
          Controls.hitButton.setDisable(true)
          Controls.newRoundButton.setDisable(false)
        case GameState.PLAYER_LOOSE =>
          balanceText.text = "Balance: " + controller.player.balance  + "$"
          currentBetText.text = "Current bet: 0$"
          statusText.text = controller.player.name + " looses!"
          Controls.standButton.setDisable(true)
          Controls.hitButton.setDisable(true)
          Controls.newRoundButton.setDisable(false)
        case GameState.PUSH =>
          statusText.text = "Push! " + controller.player.name + " and the Dealer's hand value the same "
          Controls.standButton.setDisable(true)
          Controls.hitButton.setDisable(true)
          Controls.newRoundButton.setDisable(false)

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
