package org.kaloz.pi4j.client.pi

import org.kaloz.pi4j.client.factory.ClientFactory

object PiClientFactory extends ClientFactory {

  lazy val gpio = new GpioImpl
  lazy val gpioUtil = new GpioUtilImpl
  lazy val gpioInterrupt = new GpioInterruptImpl

}
