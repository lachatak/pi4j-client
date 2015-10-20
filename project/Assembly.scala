import sbt.Keys._
import sbtassembly.AssemblyKeys._

object Assembly {

  lazy val remoteServerAssemblySettings =
    Seq(
      mainClass in assembly := Some("org.kaloz.pi4j.remote.server.Main"),
      assemblyJarName in assembly := "pi4j-remote-server.jar"
    )

  lazy val clientAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-client.jar",
      assemblyOption in assembly ~= {
        _.copy(includeScala = false, includeDependency = false)
      }
    )

}
