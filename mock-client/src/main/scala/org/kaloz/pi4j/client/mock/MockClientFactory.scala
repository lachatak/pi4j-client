package org.kaloz.pi4j.client.mock

import org.kaloz.pi4j.client.factory.ClientFactory

class MockClientFactory extends ClientFactory {

  lazy val gpio = new GpioImpl
  lazy val gpioUtil = new GpioUtilImpl
  lazy val gpioInterrupt = new GpioInterruptImpl

  def shutdown: Unit = {}
}
