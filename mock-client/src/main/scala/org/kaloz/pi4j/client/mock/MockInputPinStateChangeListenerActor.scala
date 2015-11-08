package org.kaloz.pi4j.client.mock

import akka.actor._

class MockInputPinStateChangeListenerActor(pin: Int) extends Actor with ActorLogging {

  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    log.info(s"Mock is activated for pin $pin")
  }

  override def postStop(): Unit = {
    log.info(s"Terminate mock for $pin")
  }

}

object MockInputPinStateChangeListenerActor {

  def factory: (ActorRefFactory, Int) => ActorRef = (factory, pin) => factory.actorOf(Props(classOf[MockInputPinStateChangeListenerActor], pin), s"empty-mock-$pin-actor")

}
