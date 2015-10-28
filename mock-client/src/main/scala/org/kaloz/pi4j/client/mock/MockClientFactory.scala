package org.kaloz.pi4j.client.mock

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.actor.{InMemoryClientActor, LocalInputPinStateChangedListenerActor}
import org.kaloz.pi4j.client.factory.ClientFactory

class MockClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  private val system = ActorSystem("mock-actor-system")
  private val mockClientActor = system.actorOf(InMemoryClientActor.props(MockInputPinStateChangeListenerActor.factory), "mockClientActor")

  system.actorOf(LocalInputPinStateChangedListenerActor.props, "pinStateChangeListenerActor")

  val gpio = new MockGpio(mockClientActor)
  val gpioUtil = new MockGpioUtil(mockClientActor)
  val gpioInterrupt = new MockGpioInterrupt(mockClientActor)

  def shutdown: Unit = system.terminate()
}

object MockClientFactory {
  val instance = new MockClientFactory()
}
