package org.kaloz.pi4j.client.mock

import org.kaloz.pi4j.client.factory.ClientFactory

class MockClientFactory extends ClientFactory {

  lazy val gpio = new MockGpio
  lazy val gpioUtil = new MockGpioUtil
  lazy val gpioInterrupt = new MockGpioInterrupt

  def shutdown: Unit = {}
}
