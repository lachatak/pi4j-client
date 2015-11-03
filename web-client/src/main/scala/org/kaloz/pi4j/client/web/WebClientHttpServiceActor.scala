package org.kaloz.pi4j.client.web

import akka.actor.Props
import org.kaloz.pi4j.client.Gpio

class WebClientHttpServiceActor(gpio: Gpio) {
}

object WebClientHttpServiceActor {
  def props(gpio: Gpio) = Props(classOf[WebClientHttpServiceActor], gpio)
}