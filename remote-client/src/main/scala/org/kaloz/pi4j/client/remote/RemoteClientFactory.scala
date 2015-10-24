package org.kaloz.pi4j.client.remote

import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.factory.ClientFactory

class RemoteClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  lazy val gpio = new RemoteClientGpio
  lazy val gpioUtil = new RemoteClientGpioUtil
  lazy val gpioInterrupt = new RemoteClientGpioInterrupt

  def shutdown: Unit = {}
}
