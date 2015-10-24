package org.kaloz.pi4j.client.console

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.jnativehook.GlobalScreen
import org.kaloz.pi4j.client.factory.ClientFactory

class ConsoleClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  private lazy val system = ActorSystem("console-actor-system")

  system.actorOf(PinStateChangeListenerActor.props, "pinStateChangeListenerActor")

  private val consoleInputPinStateChangeListener = ConsoleInputPinStateChangeListenerActor.factory
  private lazy val consoleClientActor = system.actorOf(ConsoleClientActor.props(consoleInputPinStateChangeListener), "consoleClientActor")

  lazy val gpio = new ConsoleGpio(consoleClientActor)
  lazy val gpioUtil = new ConsoleGpioUtil(consoleClientActor)
  lazy val gpioInterrupt = new ConsoleGpioInterrupt(consoleClientActor)

  def shutdown(): Unit = {
    GlobalScreen.unregisterNativeHook()
    system.shutdown()
  }

}
