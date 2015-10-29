package org.kaloz.pi4j.client.web

import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.factory.ClientFactory

class WebClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  lazy val gpio = new WebGpio
  lazy val gpioUtil = new WebGpioUtil
  lazy val gpioInterrupt = new WebGpioInterrupt

  def shutdown: Unit = {}
}

object WebClientFactory {
  val instance = new WebClientFactory()
}
