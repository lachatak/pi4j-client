package org.kaloz.pi4j.client.console

import akka.actor.ActorSystem
import org.kaloz.pi4j.client.factory.ClientFactory

class ConsoleClientFactory extends ClientFactory {

  private lazy val system = ActorSystem("stub-actor-system")

  private lazy val stubClientActor = system.actorOf(StubClientActor.props)

  lazy val gpio = new GpioImpl(stubClientActor)
  lazy val gpioUtil = new GpioUtilImpl(stubClientActor)
  lazy val gpioInterrupt = new GpioInterruptImpl(stubClientActor)

  def shutdown(): Unit = system.shutdown()

}
