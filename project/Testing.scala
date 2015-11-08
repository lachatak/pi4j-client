import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt._

object Testing {

  lazy val defaultSettings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )

  //Required by Aspects testing
  lazy val coreSettings = Seq(
    fork in Test := true,
    javaOptions in Test ++= Seq(BaseSettings.javaagent)
  )

  //Required by Aspects testing
  lazy val exampleSettings = Seq(
    fork in Test := true,
    testForkedParallel in Test := true,
    testGrouping in Test := oneForkedJvmPerTest((definedTests in Test).value)
  )

  lazy val multiJmvSettings = defaultSettings ++ SbtMultiJvm.multiJvmSettings ++ Seq(
    // make sure that MultiJvm test are compiled by the default test compilation
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    // make sure that MultiJvm tests are executed by the default test target,
    // and combine the results from ordinary test and multi-jvm tests
    executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
      case (testResults, multiNodeResults) =>
        val overall =
          if (testResults.overall.id < multiNodeResults.overall.id)
            multiNodeResults.overall
          else
            testResults.overall
        Tests.Output(overall,
          testResults.events ++ multiNodeResults.events,
          testResults.summaries ++ multiNodeResults.summaries)
    }
  )

  def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
    tests map {
      test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name, BaseSettings.javaagent, "-Dpi4j.client.mode=mock"))))
    }
}

