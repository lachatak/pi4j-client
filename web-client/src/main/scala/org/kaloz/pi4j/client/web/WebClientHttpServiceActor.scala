package org.kaloz.pi4j.client.web

import akka.actor.Props
import spray.routing.HttpServiceActor

class WebClientHttpServiceActor extends HttpServiceActor {
  private implicit val system = context.system

  def receive = runRoute {
    path("ping") {
      get {
        complete("pong")
      }
    }
  }
}

object WebClientHttpServiceActor {
  def props = Props[WebClientHttpServiceActor]
}
