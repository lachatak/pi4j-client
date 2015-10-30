package org.kaloz.pi4j.client.web

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl._
import org.json4s.DefaultFormats
import scala.concurrent.duration._


import scala.io.StdIn

object WebApp extends App {

  implicit val formats = DefaultFormats
  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  val myFlow: Flow[Message, Message, _] = {
    val sinkActor = actorSystem.actorOf(Props[SinkActor])

    val in = Flow[Message].to(Sink.actorRef(sinkActor, "COMPLETE-MESSAGE"))
    val out = Source.actorRef(10, OverflowStrategy.fail).mapMaterializedValue(sinkActor ! _)

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

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
  println("Server is down...")
}

class SinkActor extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global

  def receive = {
    case a: ActorRef => {
      println(s"actor-ref received: $a")
      context.system.scheduler.schedule(0 milliseconds, 1 second, a, TextMessage(s"currentTime: ${System.currentTimeMillis}"))
    }
    case x => println(s"${this}-sink: $x")
  }
}
