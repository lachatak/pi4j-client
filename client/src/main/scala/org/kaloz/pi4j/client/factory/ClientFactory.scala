package org.kaloz.pi4j.client.factory

import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}

trait ClientFactory {

  def gpio: Gpio

  def gpioUtil: GpioUtil

  def gpioInterrupt: GpioInterrupt

  def shutdown: Unit
}
