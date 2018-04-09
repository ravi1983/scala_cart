lazy val `scala-cart` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "scala-cart",
    organization := "com.tamil.pos",
    version := "1.0",
    scalaVersion := "2.12.4"
  )

resolvers += "chrisdinn" at "http://chrisdinn.github.io/releases/"

libraryDependencies ++= Seq(ws,
  guice,
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "com.google.code.gson" % "gson" % "2.8.2")