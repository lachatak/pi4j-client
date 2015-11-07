import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys._

object Publish {

  lazy val defaultSettings = Seq(
    profileName := "lachatak",
    homepage := Some(url("https://github.com/lachatak/pi4j-client")),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
    pomExtra := (
      <scm>
        <url>git@github.com:lachatak/pi4j-client.git</url>
        <connection>scm:git:git@github.com:lachatak/pi4j-client.git</connection>
      </scm>
        <developers>
          <developer>
            <id>lachatak</id>
            <name>Krisztian Lachata</name>
            <email>krisztian.lachata@gmail.com</email>
            <url>http://kaloz.org</url>
          </developer>
          <developer>
            <id>polgart</id>
            <name>Tamas Polgar</name>
            <email>tms.polgar@gmail.com</email>
          </developer>
        </developers>),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    })

  lazy val noPublishing = Seq(
    publish :=(),
    publishLocal :=()
  )
}