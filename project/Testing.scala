import sbt._
import sbt.Keys._

object Testing {

  lazy val settings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )

}
