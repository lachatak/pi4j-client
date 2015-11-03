import sbt.Keys._
import sbt._

object Version {

  val akka          = "2.4.0"
  val jodaTime      = "2.8.2"
  val scalazCore    = "7.1.3"
  val scalaTest     = "2.2.0"
  val config        = "1.2.1"
  val mockito       = "1.10.19"
  val macwire       = "1.0.5"
  val pi4j          = "1.0"
  val scalaLogging  = "3.1.0"
  val logBack       = "1.1.3"
  val rxScala       = "0.25.0"
  val aspectj       = "1.8.7"
  val jnativehook   = "2.0.2"
  val reflections   = "0.9.10"
}

object Library {
  val akkaActor         = "com.typesafe.akka"           %% "akka-actor"                    % Version.akka
  val akkaCluster       = "com.typesafe.akka"           %% "akka-cluster"                  % Version.akka
  val akkaClusterTools  = "com.typesafe.akka"           %% "akka-cluster-tools"            % Version.akka
  val akkaSlf4j         = "com.typesafe.akka"           %% "akka-slf4j"                    % Version.akka
  val akkaTestkit       = "com.typesafe.akka"           %% "akka-testkit"                  % Version.akka
  val jodaTime          = "joda-time"                   %  "joda-time"                     % Version.jodaTime
  val config            = "com.typesafe" 	              %  "config"                        % Version.config
  val scalazCore        = "org.scalaz"           	      %% "scalaz-core"                   % Version.scalazCore
  val mockito           = "org.mockito"                 %  "mockito-core"                  % Version.mockito
  val scalaTest         = "org.scalatest"               %% "scalatest"                     % Version.scalaTest
  val pi4jCore          = "com.pi4j"                    %  "pi4j-core"                     % Version.pi4j
  val scalaLogging      = "com.typesafe.scala-logging"  %% "scala-logging"                 % Version.scalaLogging
  val logBack           = "ch.qos.logback"              %  "logback-classic"               % Version.logBack
  val rxScala           = "io.reactivex"                %% "rxscala"                       % Version.rxScala
  val aspectjweaver     = "org.aspectj"                 %  "aspectjweaver"                 % Version.aspectj
  val aspectjrt         = "org.aspectj"                 %  "aspectjrt"                     % Version.aspectj
  val jnativehook       = "com.1stleg"                  %  "jnativehook"                   % Version.jnativehook
  val reflections       = "org.reflections"             %  "reflections"                   % Version.reflections
}

object Dependencies {

  import Library._

  val core = deps(
    config,
    akkaActor,
    reflections,
    scalazCore,
    aspectjrt,
    aspectjweaver,
    pi4jCore,
    scalaLogging,
    logBack,
    akkaTestkit       % "test",
    mockito       	  % "test",
    scalaTest     	  % "test"
  )

  val api = deps(
    scalazCore,
    pi4jCore,
    scalaLogging,
    logBack,
    mockito       	  % "test",
    scalaTest     	  % "test"
  )

  val console = deps(
    config,
    akkaActor,
    jnativehook,
    pi4jCore,
    logBack,
    akkaTestkit     % "test",
    mockito       	% "test",
    scalaTest     	% "test"
  )

  val web = deps(
    config,
    pi4jCore,
    logBack,
    mockito       	% "test",
    scalaTest     	% "test"
  )

  val mock = deps(
    mockito       	% "test",
    scalaTest     	% "test"
  )

  val remoteClient = deps(
    config,
    akkaCluster,
    akkaClusterTools,
    pi4jCore,
    logBack,
    config,
    akkaTestkit     % "test",
    mockito       	% "test",
    scalaTest     	% "test"
  )

  val remoteServer = deps(
    config,
    aspectjweaver,
    aspectjrt,
    akkaCluster,
    akkaClusterTools,
    scalaLogging,
    pi4jCore,
    logBack,
    mockito       	% "test",
    scalaTest     	% "test"
  )

  private def deps(modules: ModuleID*): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
}

