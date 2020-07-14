package de.htwg.se.blackjackKN.gamelogic

import com.google.inject.AbstractModule
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.ControllerInterface
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.GameManagerPersistenceInterface
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation.GameManagerPersistence
import net.codingwell.scalaguice.ScalaModule

class BlackjackModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
    bind[GameManagerPersistenceInterface].to[GameManagerPersistence]
  }

}
