package de.htwg.se.blackjackKN.gamelogic

import com.google.inject.AbstractModule
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.ControllerInterface
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl.Controller
import net.codingwell.scalaguice.ScalaModule

class BlackjackModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
  }

}
