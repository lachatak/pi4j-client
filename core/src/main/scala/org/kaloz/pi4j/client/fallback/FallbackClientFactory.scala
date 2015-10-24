package org.kaloz.pi4j.client.fallback

import org.kaloz.pi4j.client.factory.ClientFactory

class FallbackClientFactory extends ClientFactory {

  lazy val gpio = new FallbackGpio
  lazy val gpioUtil = new FallbackGpioUtil
  lazy val gpioInterrupt = new FallbackGpioInterrupt

  def shutdown(): Unit = {}

}
