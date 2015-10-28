package org.kaloz.pi4j.client.web

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.{GpioInterruptActorGateway, GpioUtilActorGateway, GpioActorGateway}
import org.kaloz.pi4j.client.factory.ClientFactory
import spray.can.Http

class WebClientFactory extends ClientFactory with StrictLogging {
  private implicit val timeout = Timeout(1 minute)
  private implicit val system = ActorSystem("web-actor-system")

  private val serviceActor = system.actorOf(WebClientHttpServiceActor.props(gpio))
  private val httpBind = IO(Http) ? Http.Bind(serviceActor, "localhost", 9000)

  logger.info("Initialised...")

  //TODO add proper client actor
  lazy val gpio = new GpioActorGateway(null)
  lazy val gpioUtil = new GpioUtilActorGateway(null)
  lazy val gpioInterrupt = new GpioInterruptActorGateway(null)

  def shutdown: Unit = {
//    IO(Http) ! Http.Unbind
  }
}

object WebClientFactory {
  val instance = new WebClientFactory()
}
