import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._

object Assembly {

  lazy val remoteServerAssemblySettings =
    Seq(
      name := "pi4j-remote-server",
      mainClass in assembly := Some("org.kaloz.pi4j.server.remote.Main"),
      assemblyJarName in assembly <<= (name, version) map ((x, y) => "%s-%s-assembly.jar" format(x, y)),
      artifact in(Compile, assembly) := {
        val art = (artifact in(Compile, assembly)).value
        art.copy(`classifier` = Some("assembly"))
      }
    ) ++
      addArtifact(Artifact("remote-server", "assembly"), sbtassembly.AssemblyKeys.assembly)

}