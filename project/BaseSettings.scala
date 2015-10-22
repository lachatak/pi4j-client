import sbt.Keys._
import sbt._

object BaseSettings {

  lazy val defaultSettings =
    Seq(
      version := "1.0.0",
      organization := "org.kaloz.pi4j.client",
      description := "Pi4j Client Project for stubing, remoting and mocking",
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
      shellPrompt := { s => "[" + scala.Console.BLUE + Project.extract(s).currentProject.id + scala.Console.RESET + "] $ " }
    ) ++
      ResolverSettings.settings ++
      Aliases.aliases

  //Required by Aspects
  lazy val coreSettings = defaultSettings ++ Seq(
    javacOptions += "-g"
  )

  //Required by Aspects
  lazy val exampleSettings = defaultSettings ++ Seq(
    javaOptions in run ++= Seq("-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-" + Version.aspectj + ".jar"),
    fork in run := true
  )
}
