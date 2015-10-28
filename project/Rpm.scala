import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport._
import com.typesafe.sbt.packager.linux.LinuxSymlink
import com.typesafe.sbt.packager.rpm.RpmPlugin.autoImport.{Rpm => RpmConfig, _}
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport.Universal
import sbt.Keys._
import sbt._

object Rpm {

  lazy val rpmSettings =
    Seq(
      mainClass in(Compile, run) := Some("org.kaloz.pi4j.server.remote.Main"),
      maintainer in Linux := "Krisztian Lachata",

      packageSummary in Linux := "pi4j-remote-server",
      packageDescription := "Pi4j Client Project - Remote Server",
      rpmUrl := Some("https://github.com/lachatak/pi4j-client"),

      rpmVendor := "http://kaloz.org",
      rpmLicense := Some("Apache-2.0"),
      rpmPost := None,

      version in RpmConfig := version.value.replace('-', '_'),

      rpmRelease := "1",

      defaultLinuxInstallLocation := "/var/kaloz",
      defaultLinuxLogsLocation := "/var/kaloz/logs",
      linuxPackageSymlinks += LinuxSymlink("/var/log/pi4j-remote-server", "/var/kaloz/logs/pi4j-remote-server"),

      javaOptions in Universal ++= Seq(
        "-javaagent:/var/kaloz/pi4j-remote-server/jars/aspectjweaver.jar",
        "-Dconfig.file=/var/kaloz/pi4j-remote-server/conf/pi4j-remote-server.conf",
        "-Dlogback.configurationFile=/var/kaloz/pi4j-remote-server/conf/logback.xml"
      )
    )
}