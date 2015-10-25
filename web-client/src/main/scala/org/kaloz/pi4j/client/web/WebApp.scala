package org.kaloz.pi4j.client.web

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object WebApp extends App {
  implicit val timeout = Timeout(1 minute)
  implicit val system = ActorSystem("web-actor-system")

  val serviceActor = system.actorOf(WebClientHttpServiceActor.props(new WebGpio))
  val httpBind = IO(Http) ? Http.Bind(serviceActor, "localhost", 9000)
}
