package org.kaloz.pi4j.client.mock

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.console._
import org.kaloz.pi4j.client.factory.ClientFactory

class MockClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  private lazy val system = ActorSystem("mock-actor-system")

  system.actorOf(PinStateChangeListenerActor.props, "pinStateChangeListenerActor")

  private val mockInputPinStateChangeListener = MockInputPinStateChangeListenerActor.factory
  private lazy val mockClientActor = system.actorOf(ConsoleClientActor.props(mockInputPinStateChangeListener), "mockClientActor")

  lazy val gpio = new ConsoleGpio(mockClientActor)
  lazy val gpioUtil = new ConsoleGpioUtil(mockClientActor)
  lazy val gpioInterrupt = new ConsoleGpioInterrupt(mockClientActor)

  def shutdown: Unit = system.shutdown()
}
