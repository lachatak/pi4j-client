package org.kaloz.pi4j.client.mock

import akka.actor._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback

class MockInputPinStateChangeListenerActorFactory extends Actor with ActorLogging {

  override def receive: Receive = {
    case CreatePinStateChangeCallback(pin, parent) => sender ! self
  }

}

object MockInputPinStateChangeListenerActorFactory {

  def props = Props(classOf[MockInputPinStateChangeListenerActorFactory])

}
