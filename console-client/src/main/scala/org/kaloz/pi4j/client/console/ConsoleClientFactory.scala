package org.kaloz.pi4j.client.console

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.actor.{InMemoryClientActor, LocalInputPinStateChangedListenerActor}
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.{GpioActorGateway, GpioInterruptActorGateway, GpioUtilActorGateway}

class ConsoleClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initializing...")

  private implicit val actorSystem = ActorSystem("console-actor-system")

  private val consoleInputPinStateChangeListenerActorFactory = actorSystem.actorOf(ConsoleInputPinStateChangeListenerActorFactory.props(), "consoleInputPinStateChangeListenerActorFactory")
  private val consoleClientActor = actorSystem.actorOf(InMemoryClientActor.props(consoleInputPinStateChangeListenerActorFactory), "consoleClientActor")

  actorSystem.actorOf(LocalInputPinStateChangedListenerActor.props(), "pinStateChangeListenerActor")

  val gpio = new GpioActorGateway(consoleClientActor)
  val gpioUtil = new GpioUtilActorGateway(consoleClientActor)
  val gpioInterrupt = new GpioInterruptActorGateway(consoleClientActor)

  def shutdown(): Unit = actorSystem.terminate()

}

object ConsoleClientFactory {
  val instance = new ConsoleClientFactory()
}
