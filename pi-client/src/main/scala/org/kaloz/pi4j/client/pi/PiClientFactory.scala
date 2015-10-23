package org.kaloz.pi4j.client.pi

import org.kaloz.pi4j.client.factory.ClientFactory

class PiClientFactory extends ClientFactory {

  lazy val gpio = new PiGpio
  lazy val gpioUtil = new PiGpioUtil
  lazy val gpioInterrupt = new PiGpioInterrupt

  def shutdown: Unit = {}
}
