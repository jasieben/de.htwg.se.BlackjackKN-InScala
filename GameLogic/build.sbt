name          := "GameLogic"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.12.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

coverageExcludedPackages := ".*gui.*"

libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.2-R18"
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)


libraryDependencies += "junit" % "junit" % "4.8" % "test"

//*******************************************************************************//
//Libraries that we will use in later lectures compatible with this scala version
// uncomment to use!!

//libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.1"

libraryDependencies += "com.google.inject" % "guice" % "4.2.2"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.2"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.12"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.5"
libraryDependencies += "de.heikoseeberger" %% "akka-http-play-json" % "1.32.0"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.1",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.1"
)

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.13"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"
