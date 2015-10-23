import sbt.Keys._
import sbtassembly.AssemblyKeys._

object Assembly {

  private lazy val assemblySettings = Seq(
    assemblyOption in assembly ~= {
      _.copy(includeScala = false, includeDependency = false)
    }
  )

  lazy val coreAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-client-core.jar"
    ) ++ assemblySettings

  lazy val consoleAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-console-client.jar"
    ) ++ assemblySettings

  lazy val webAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-web-client.jar"
    ) ++ assemblySettings

  lazy val mockAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-mock-client.jar"
    ) ++ assemblySettings

  lazy val remoteClientAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-remote-client.jar"
    ) ++ assemblySettings

  lazy val remoteServerAssemblySettings =
    Seq(
      mainClass in assembly := Some("org.kaloz.pi4j.remote.server.Main"),
      assemblyJarName in assembly := "pi4j-remote-server.jar"
    )

  lazy val piClientAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-pi-client.jar"
    ) ++ assemblySettings

  lazy val exampleAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-client-examples.jar"
    )

}
