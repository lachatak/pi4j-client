import com.typesafe.sbt.SbtPgp.PgpKeys._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleasePlugin._

import scala.util.Properties.{propOrEmpty, propOrNone}

object Release {

  lazy val settings = releaseSettings ++ Seq(
    publishArtifactsAction := publishSigned.value,
    releaseVersion := { _ => propOrEmpty("releaseVersion") },
    nextVersion := { _ => propOrEmpty("developmentVersion") },
    pgpPassphrase := propOrNone("gpg.passphrase").map(_.toCharArray)
  )

}