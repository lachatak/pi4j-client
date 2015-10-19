import sbt._

object Aliases {

  lazy val controlGpioExample = addCommandAlias("controlGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ControlGpioExample")
  lazy val listenGpioExample = addCommandAlias("listenGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ListenGpioExample")
  lazy val shutdownGpioExample = addCommandAlias("shutdownGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ShutdownGpioExample")
  lazy val triggerGpioExample = addCommandAlias("triggerGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.TriggerGpioExample")
}
