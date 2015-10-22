package org.kaloz.pi4j.client.web

import org.kaloz.pi4j.client.factory.ClientFactory

class WebClientFactory extends ClientFactory {

  lazy val gpio = new GpioImpl
  lazy val gpioUtil = new GpioUtilImpl
  lazy val gpioInterrupt = new GpioInterruptImpl

  def shutdown: Unit = {}
}
