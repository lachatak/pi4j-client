package org.kaloz.pi4j.client.web

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.actor.InMemoryClientActor
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.web.WebSocketActor._
import org.kaloz.pi4j.client.{GpioActorGateway, GpioInterruptActorGateway, GpioUtilActorGateway}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class WebClientFactory extends ClientFactory with StrictLogging with Configuration {

  private implicit val timeout = Timeout(1 minute)
  private implicit val system = ActorSystem("web-actor-system")
  private implicit val flowMaterializer = ActorMaterializer()

  private val webSocketActor = system.actorOf(Props[WebSocketActor], "webSocketActor")
  private val webClientActor = system.actorOf(InMemoryClientActor.props(WebSocketActor.f(webSocketActor)), "webClientActor")

  private val route = {
    path("pi4jWebSocket") {
      get {
        handleWebsocketMessages(webSocketActorFlow(webSocketActor, webClientActor))
      }
    } ~ pathPrefix("www") {
      getFromResourceDirectory("www")
    }
  }

  private val binding = Http().bindAndHandle(route, "0.0.0.0", webClientPort)

  binding.foreach { address =>
    logger.info(s"Initialised on $address...")
  }

  val gpio = new GpioActorGateway(webClientActor)
  val gpioUtil = new GpioUtilActorGateway(webClientActor)
  val gpioInterrupt = new GpioInterruptActorGateway(webClientActor)

  def shutdown: Unit = {
    binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

object WebClientFactory {
  val instance = new WebClientFactory()
}
