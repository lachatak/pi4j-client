package org.kaloz.pi4j.client.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.DefaultFormats

import scala.io.StdIn

object WebApp extends App {

  implicit val formats = DefaultFormats
  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(text) => TextMessage(doStuff(parse(text)))
    case _ => TextMessage("Message type unsupported")
  }

  def doStuff(json: JValue): String = (json \ "method").extractOpt[String] match {
    case Some("writeDigital") => "writeDigital"
  }

  val route =
    path("ws-echo") {
      get {
        handleWebsocketMessages(echoService)
      }
    }

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
  println("Server is down...")
}