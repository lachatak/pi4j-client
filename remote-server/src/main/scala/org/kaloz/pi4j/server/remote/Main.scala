package org.kaloz.pi4j.server.remote

import akka.actor.ActorSystem
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.sys.addShutdownHook

object Main extends App with Configuration {

  val system = ActorSystem("pi4j-remoting", config)

  val remoteServerActor = system.actorOf(RemoteServerActor.props, "remoteServerActor")

  def pinStateChangeCallback(pin: Int, state: java.lang.Boolean): Unit = remoteServerActor ! DigitalInputPinValueChangedEvent(pin, state)

  addShutdownHook {
    Await.ready(system.terminate(), 30 second)
  }
}
