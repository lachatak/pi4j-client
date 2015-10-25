package org.kaloz.pi4j.client.remote

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.factory.ClientFactory

class RemoteClientFactory extends ClientFactory with Configuration with StrictLogging {

  logger.info("Initialised...")

  private lazy val system = ActorSystem("Pi4jRemoting", config)
  private lazy val remoteClientActor = system.actorOf(RemoteClientActor.props)

  lazy val gpio = new RemoteClientGpio(remoteClientActor)
  lazy val gpioUtil = new RemoteClientGpioUtil(remoteClientActor)
  lazy val gpioInterrupt = new RemoteClientGpioInterrupt(remoteClientActor)

  def shutdown: Unit = system.shutdown()
}
