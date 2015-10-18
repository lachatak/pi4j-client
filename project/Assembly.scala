import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.{PathList, MergeStrategy}

object Assembly {

  lazy val pi4jRemoteServerAssemblySettings =
  Seq(
    mainClass in assembly := Some("org.kaloz.pi4j.remote.server.Main"),
    assemblyJarName in assembly := "pi4j-client-remote-server.jar"
  )

  lazy val pi4jClientAssemblySettings =
  Seq(
    assemblyJarName in assembly := "pi4j-client.jar",
    assemblyOption in assembly ~= { _.copy(includeScala = false, includeDependency=false) }
  )

  lazy val pi4jCommonAssemblySettings =
    Seq(
      assemblyJarName in assembly := "pi4j-client-common.jar",
      assemblyOption in assembly ~= { _.copy(includeScala = false, includeDependency=false) }
    )
}
