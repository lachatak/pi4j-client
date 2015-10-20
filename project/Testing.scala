import sbt.Keys._
import sbt._

object Testing {

  lazy val settings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )

  lazy val clientSettings = Seq(
    javaOptions in(Test, run) ++= Seq("-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.8.7.jar", "-Dpi4j.client.mode=stub"),
    fork in(Test, run) := true
  )

}
