package org.kaloz.pi4j.server.remote

import akka.actor.ActorSystem
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.InputPinStateChanged

object Main extends App with Configuration {

  val system = ActorSystem("pi4j-remoting", config)

  val remoteServerActor = system.actorOf(RemoteServerActor.props, "remoteServerActor")

  def pinStateChangeCallback(pin: Int, state: java.lang.Boolean): Unit = remoteServerActor ! InputPinStateChanged(pin, state)
}
