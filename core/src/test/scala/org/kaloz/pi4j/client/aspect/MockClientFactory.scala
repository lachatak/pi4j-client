package org.kaloz.pi4j.client.aspect

import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}
import org.scalatest.mock.MockitoSugar

class MockClientFactory extends ClientFactory with MockitoSugar {

  val gpio = mock[Gpio]
  val gpioUtil = mock[GpioUtil]
  val gpioInterrupt = mock[GpioInterrupt]

  def shutdown(): Unit = {}

}

object MockClientFactory {
  val instance = new MockClientFactory()
}
