import sbt._

object Aliases {

  lazy val aliases =
    addCommandAlias("controlGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ControlGpioExample") ++
    addCommandAlias("listenGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ListenGpioExample") ++
    addCommandAlias("shutdownGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.ShutdownGpioExample") ++
    addCommandAlias("triggerGpioExample", ";project client; test:runMain org.kaloz.pi4j.client.TriggerGpioExample")
}
