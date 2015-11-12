package org.kaloz.pi4j.client.web

import akka.actor._
import akka.event.LoggingReceive
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback

class WebInputPinStateChangeListenerActorFactory(webSocketActor: ActorRef) extends Actor with ActorLogging with Configuration {

  override def receive: Actor.Receive = LoggingReceive {
    case CreatePinStateChangeCallback(_, parent) => sender ! webSocketActor
  }
}

object WebInputPinStateChangeListenerActorFactory {

  def props(webSocketActor: ActorRef) = Props(classOf[WebInputPinStateChangeListenerActorFactory], webSocketActor)
}

