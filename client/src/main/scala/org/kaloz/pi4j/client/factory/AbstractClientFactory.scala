package org.kaloz.pi4j.client.factory

import org.kaloz.pi4j.client.pi.PiClientFactory
import org.kaloz.pi4j.client.remote.RemoteClientFactory
import org.kaloz.pi4j.client.stub.StubClientFactory
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}


object AbstractClientFactory extends ClientFactory {

  lazy val factory: ClientFactory =
    System.getProperty("pi4j.client.mode", "pi") match {
      case "pi" => PiClientFactory
      case "stub" => StubClientFactory
      case "remote" => RemoteClientFactory
    }

  lazy val gpio: Gpio = factory.gpio
  lazy val gpioUtil: GpioUtil = factory.gpioUtil
  lazy val gpioInterrupt: GpioInterrupt = factory.gpioInterrupt

  def shutdown(): Unit = factory.shutdown
}
