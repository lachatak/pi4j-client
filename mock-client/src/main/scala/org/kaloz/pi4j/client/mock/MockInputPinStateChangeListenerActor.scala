package org.kaloz.pi4j.client.mock

import akka.actor._

class MockInputPinStateChangeListenerActor(pin: Int) extends Actor with ActorLogging {

  log.info(s"Mock for $pin created...")

  override def receive: Receive = Actor.emptyBehavior

}

object MockInputPinStateChangeListenerActor {

  def factory: (ActorRefFactory, Int) => ActorRef = (factory, pin) => factory.actorOf(Props(classOf[MockInputPinStateChangeListenerActor], pin), s"empty-mock-$pin-actor")

}
