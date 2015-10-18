package org.kaloz.pi4j.client.stub

import akka.actor.ActorSystem
import org.kaloz.pi4j.client.factory.ClientFactory

object StubClientFactory extends ClientFactory {

  lazy val system = ActorSystem("stub-actor-system")

  lazy val stubClientActor = system.actorOf(StubClientActor.props)

  lazy val gpio = new GpioImpl(stubClientActor)
  lazy val gpioUtil = new GpioUtilImpl(stubClientActor)
  lazy val gpioInterrupt = new GpioInterruptImpl(stubClientActor)

}
