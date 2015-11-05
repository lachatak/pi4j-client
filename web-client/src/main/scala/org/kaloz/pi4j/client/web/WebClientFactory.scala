package org.kaloz.pi4j.client.web

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.actor.{InMemoryClientActor, LocalInputPinStateChangedListenerActor}
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.web.WebSocketActor.Initialize
import org.kaloz.pi4j.client.{GpioActorGateway, GpioInterruptActorGateway, GpioUtilActorGateway}
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.{OutputPinStateChanged, InputPinStateChanged}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class WebClientFactory extends ClientFactory with StrictLogging with Configuration {

  private implicit val system = ActorSystem("web-actor-system")
  private implicit val flowMaterializer = ActorMaterializer()
  private implicit val timeout = Timeout(1 minute)

  private val webSocketActor = system.actorOf(Props[WebSocketActor], "webSocketActor")
  private val webClientActor = system.actorOf(InMemoryClientActor.props(WebSocketActor.f(webSocketActor)), "webClientActor")
  system.actorOf(LocalInputPinStateChangedListenerActor.props, "pinStateChangeListenerActor")

  system.eventStream.subscribe(webSocketActor, classOf[InputPinStateChanged])
  system.eventStream.subscribe(webSocketActor, classOf[OutputPinStateChanged])

  private def webSocketActorFlow(webSocketActor: ActorRef): Flow[Message, Message, _] = {
    val in = Flow[Message].to(Sink.actorRef(webSocketActor, "onCompleteMessage"))

    val out = Source.actorRef(10, OverflowStrategy.fail).mapMaterializedValue(sink => webSocketActor ! Initialize(sink, webClientActor))
    Flow.wrap(in, out)(Keep.both)
  }

  private val route =
    path("ws-echo") {
      get {
        handleWebsocketMessages(webSocketActorFlow(webSocketActor))
      }
    } ~ pathPrefix("www") {
      getFromResourceDirectory("www")
    }

  private val binding = Http().bindAndHandle(route, "localhost", port)

  logger.info("Initialised...")

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

class WebSocketActor extends Actor with StrictLogging {

  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case x => logger.error(s"received $x")
//    case Initialize(sink, webClient) => {
//      (webClient ? PinStatesRequest).foreach(println)
//      context.system.scheduler.schedule(0 second, 5 seconds, sink, TextMessage( """{"type": "pinStateChange", "pinId": 1, "pinValue": "out-high"}"""))
//      context.become(receiveWithSink(sink))
//    }
  }

  def receiveWithSink(actorRef: ActorRef): Receive = {
    case "bar" => sender() ! "I am already happy :-)"
  }
}

object WebSocketActor {

  def f(webSocketActor: ActorRef)(x: ActorRefFactory, y: Int): ActorRef = {
    println("something happened")
    webSocketActor
  }

  case class Initialize(sink: ActorRef, webClient: ActorRef)

}
