name := "scala-cart"

version := "0.1"

scalaVersion := "2.12.4"

lazy val `scala-cart` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

resolvers += "chrisdinn" at "http://chrisdinn.github.io/releases/"

libraryDependencies ++= Seq(ws,
  guice,
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "com.google.code.gson" % "gson" % "2.8.2")