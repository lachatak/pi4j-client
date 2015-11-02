package org.kaloz.pi4j.client.web

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.pattern.ask
import akka.stream.scaladsl._
import akka.util.Timeout
import org.json4s.DefaultFormats
import org.kaloz.pi4j.client.actor.InMemoryClientActor
import org.kaloz.pi4j.client.web.SinkActor.RegisterSink
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages.WiringPiSetupRequest
import scala.concurrent.duration._


import scala.io.StdIn

object WebApp extends App {

  implicit val formats = DefaultFormats
  implicit val system = ActorSystem("web-client-actor-system")
  private val sinkActor = system.actorOf(Props[SinkActor])
  private val webClientActor = system.actorOf(InMemoryClientActor.props((x,y) => sinkActor), "webClientActor")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  val myFlow: Flow[Message, Message, _] = {
    val in = Flow[Message].to(Sink.actorRef(sinkActor, "onCompleteMessage"))
    val out = Source.actorRef(10, OverflowStrategy.fail).mapMaterializedValue(sinkActor ! RegisterSink(_, webClientActor))

    Flow.wrap(in, out)(Keep.both)
  }

  val route =
    path("ws-echo") {
      get {
        handleWebsocketMessages(myFlow)
      }
    } ~ pathPrefix("www") {
      getFromResourceDirectory("www")
    }

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import system.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  println("Server is down...")
}

class SinkActor extends Actor {
  import SinkActor._
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)
  def receive = {
    case RegisterSink(actorRef, ar2) => {
      (ar2 ? WiringPiSetupRequest).foreach(println)
      context.system.scheduler.schedule(0 second, 5 seconds, actorRef, TextMessage("""{"type": "pinStateChange", "pinId": 1, "pinValue": "out-high"}"""))
//      context.become(receiveWithSink(actorRef))
    }
  }

  def receiveWithSink(actorRef: ActorRef): Receive = {
    case "bar" => sender() ! "I am already happy :-)"
  }
}

object SinkActor {
  case class RegisterSink(actorRef: ActorRef, actorRef2: ActorRef)
}
