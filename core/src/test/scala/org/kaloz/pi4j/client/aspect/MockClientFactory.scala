package org.kaloz.pi4j.client.aspect

import com.pi4j.io.gpio.GpioController
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}
import org.scalatest.mock.MockitoSugar

class MockClientFactory extends ClientFactory with MockitoSugar {

  val gpio = mock[Gpio]
  val gpioUtil = mock[GpioUtil]
  val gpioInterrupt = mock[GpioInterrupt]
  val gpioController = mock[GpioController]

  def shutdown(): Unit = gpioController.shutdown()

}

object MockClientFactory {
  val instance = new MockClientFactory()
}
