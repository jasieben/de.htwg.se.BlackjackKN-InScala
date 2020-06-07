lazy val root = (project in file(".")).aggregate(gamelogic, playermanagement)
lazy val gamelogic = (project in file("GameLogic"))
lazy val playermanagement = (project in file("PlayerManagement"))