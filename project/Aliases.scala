import sbt._

object Aliases {

  lazy val aliases =
    addCommandAlias("controlGpioExample", ";project examples; ;clean ;runMain org.kaloz.pi4j.client.examples.ControlGpioExample") ++
      addCommandAlias("listenGpioExample", ";project examples ;clean ;runMain org.kaloz.pi4j.client.examples.ListenGpioExample") ++
      addCommandAlias("shutdownGpioExample", ";project examples ;clean ;runMain org.kaloz.pi4j.client.examples.ShutdownGpioExample") ++
      addCommandAlias("triggerGpioExample", ";project examples ;clean ;runMain org.kaloz.pi4j.client.examples.TriggerGpioExample") ++
      addCommandAlias("webApp", ";project web-client ;clean ;runMain org.kaloz.pi4j.client.web.WebApp")
}
