package org.kaloz.pi4j.client.mock

import akka.actor._

class MockInputPinStateChangeListenerActor(pin: Int, key: Char) extends Actor with ActorLogging {

  log.info(s"Mock for $pin created...")
  override def receive: Receive = Actor.emptyBehavior

}

object MockInputPinStateChangeListenerActor {

  def factory: (ActorRefFactory, Int, Char) => ActorRef = (factory, pin, char) => factory.actorOf(Props(classOf[MockInputPinStateChangeListenerActor], pin, char), s"empty-mock-$pin-$char-actor")

}
