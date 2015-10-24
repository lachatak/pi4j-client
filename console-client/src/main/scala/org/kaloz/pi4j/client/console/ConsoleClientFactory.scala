package org.kaloz.pi4j.client.console

import akka.actor.ActorSystem
import org.kaloz.pi4j.client.factory.ClientFactory

class ConsoleClientFactory extends ClientFactory {

  private lazy val system = ActorSystem("console-actor-system")

  private val pinStateChangeHandler = ConsoleListenerActor.props
  private lazy val consoleClientActor = system.actorOf(ConsoleClientActor.props(pinStateChangeHandler), "consoleClientActor")

  lazy val gpio = new ConsoleGpio(consoleClientActor)
  lazy val gpioUtil = new ConsoleGpioUtil(consoleClientActor)
  lazy val gpioInterrupt = new ConsoleGpioInterrupt(consoleClientActor)

  def shutdown(): Unit = system.shutdown()

}
