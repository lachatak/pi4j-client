package org.kaloz.pi4j.client.web

import org.kaloz.pi4j.client.factory.ClientFactory

class WebClientFactory extends ClientFactory {

  lazy val gpio = new WebGpio
  lazy val gpioUtil = new WebGpioUtil
  lazy val gpioInterrupt = new WebGpioInterrupt

  def shutdown: Unit = {}
}
