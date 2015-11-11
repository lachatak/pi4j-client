package org.kaloz.pi4j.client.mock

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.actor.{InMemoryClientActor, LocalInputPinStateChangedListenerActor}
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.{GpioActorGateway, GpioInterruptActorGateway, GpioUtilActorGateway}

class MockClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initializing...")

  private val system = ActorSystem("mock-actor-system")

  private val mockInputPinStateChangeListenerActorFactory = system.actorOf(MockInputPinStateChangeListenerActorFactory.props, "mockInputPinStateChangeListenerActorFactory")
  private val mockClientActor = system.actorOf(InMemoryClientActor.props(mockInputPinStateChangeListenerActorFactory), "mockClientActor")

  system.actorOf(LocalInputPinStateChangedListenerActor.props(), "pinStateChangeListenerActor")

  val gpio = new GpioActorGateway(mockClientActor)
  val gpioUtil = new GpioUtilActorGateway(mockClientActor)
  val gpioInterrupt = new GpioInterruptActorGateway(mockClientActor)

  def shutdown: Unit = system.terminate()
}

object MockClientFactory {
  val instance = new MockClientFactory()
}
