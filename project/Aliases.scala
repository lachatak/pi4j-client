import sbt._

object Aliases {

  lazy val aliases =
    addCommandAlias("controlGpioExample", ";project examples; runMain org.kaloz.pi4j.client.examples.ControlGpioExample") ++
      addCommandAlias("listenGpioExample", ";project examples; runMain org.kaloz.pi4j.client.examples.ListenGpioExample") ++
      addCommandAlias("shutdownGpioExample", ";project examples; runMain org.kaloz.pi4j.client.examples.ShutdownGpioExample") ++
      addCommandAlias("triggerGpioExample", ";project examples; runMain org.kaloz.pi4j.client.examples.TriggerGpioExample")
}
