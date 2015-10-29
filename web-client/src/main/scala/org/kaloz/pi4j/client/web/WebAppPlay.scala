package org.kaloz.pi4j.client.web

import akka.actor.{Actor, ActorRef, Props}
import play.api.Play.current
import play.api.mvc._
import play.api.routing.sird._
import play.core.server._

object WebAppPlay extends App {
  val server = NettyServer.fromRouter(ServerConfig(
    port = Some(9000),
    address = "127.0.0.1"
  )) {
    case GET(p"/hello/$to") => Action {
      Results.Ok(s"Hello $to")
    }
//    case GET(p"/ws") => WebSocket.acceptWithActor[String, String] { request => out =>
//      MyWebSocketActor.props(out)
//    }
  }
}
//
//object MyWebSocketActor {
//  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
//}
//
//class MyWebSocketActor(out: ActorRef) extends Actor {
//  def receive = {
//    case msg: String =>
//      out ! ("I received your message: " + msg)
//  }
//}