name          := "GameLogic"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.12.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

//*******************************************************************************//
//Libraries that we will use in later lectures compatible with this scala version
// uncomment to use!!

//libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.1"

libraryDependencies += "com.google.inject" % "guice" % "4.2.2"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.2"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.3"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

test in assembly := {}
