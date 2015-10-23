package org.kaloz.pi4j.client.remote

import org.kaloz.pi4j.client.factory.ClientFactory

class RemoteClientFactory extends ClientFactory {

  lazy val gpio = new RemoteClientGpio
  lazy val gpioUtil = new RemoteClientGpioUtil
  lazy val gpioInterrupt = new RemoteClientGpioInterrupt

  def shutdown: Unit = {}
}
