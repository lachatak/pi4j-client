package org.kaloz.pi4j.client.factory

import com.pi4j.io.gpio.GpioController
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}
import org.scalatest.mock.MockitoSugar

class ConsoleClientFactory extends ClientFactory with MockitoSugar {

  val gpio = mock[Gpio]
  val gpioUtil = mock[GpioUtil]
  val gpioInterrupt = mock[GpioInterrupt]
  val gpioController = mock[GpioController]

  def shutdown(): Unit = gpioController.shutdown()
}

object ConsoleClientFactory {
  val instance = new ConsoleClientFactory()
}

class OtherClientFactory extends ClientFactory with MockitoSugar {

  val gpio = mock[Gpio]
  val gpioUtil = mock[GpioUtil]
  val gpioInterrupt = mock[GpioInterrupt]
  val gpioController = mock[GpioController]

  def shutdown(): Unit = gpioController.shutdown()
}

object OtherClientFactory {
  val instance = new OtherClientFactory()
}

