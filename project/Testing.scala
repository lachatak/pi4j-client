import sbt.Keys._
import sbt._

object Testing {

  lazy val settings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )

  //Required by Aspects testing
  lazy val coreSettings = Seq(
    javaOptions in Test  ++= Seq("-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-" + Version.aspectj + ".jar"),
    fork in Test := true,
    parallelExecution in Test := false
  )
}
