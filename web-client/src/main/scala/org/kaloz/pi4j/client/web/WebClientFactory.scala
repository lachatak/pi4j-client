package org.kaloz.pi4j.client.web

import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.{GpioInterruptActorGateway, GpioUtilActorGateway, GpioActorGateway}
import org.kaloz.pi4j.client.factory.ClientFactory

class WebClientFactory extends ClientFactory with StrictLogging {

  logger.info("Initialised...")

  //TODO add proper client actor
  lazy val gpio = new GpioActorGateway(null)
  lazy val gpioUtil = new GpioUtilActorGateway(null)
  lazy val gpioInterrupt = new GpioInterruptActorGateway(null)

  def shutdown: Unit = {}
}

object WebClientFactory {
  val instance = new WebClientFactory()
}
