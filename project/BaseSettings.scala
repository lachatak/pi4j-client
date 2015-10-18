import sbt._
import Keys._

object BaseSettings {

  lazy val settings =
  Seq(
    version := "1.0.0",
    organization := "org.kaloz.pi4j.client",
    description := "Pi4j Client Project for remoting and stubing",
    scalaVersion := "2.11.7",
    homepage := Some(url("http://kaloz.org")),
    scalacOptions := Seq(
      "-encoding", "utf8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.8",
      "-language:postfixOps",
      "-language:implicitConversions"
    ),
    javacOptions := Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    ),
    shellPrompt := { s => "[" + scala.Console.BLUE + Project.extract(s).currentProject.id + scala.Console.RESET + "] $ "}
  ) ++
  ResolverSettings.settings ++
  Testing.settings

}
