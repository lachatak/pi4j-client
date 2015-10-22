package org.kaloz.pi4j.client.factory

import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}

object AbstractClientFactory extends ClientFactory with Configuration {

  lazy val factory: ClientFactory = Class.forName(classByType(System.getProperty("pi4j.client.mode", "console"))).newInstance().asInstanceOf[ClientFactory]

  lazy val gpio: Gpio = factory.gpio
  lazy val gpioUtil: GpioUtil = factory.gpioUtil
  lazy val gpioInterrupt: GpioInterrupt = factory.gpioInterrupt

  def shutdown(): Unit = factory.shutdown
}
