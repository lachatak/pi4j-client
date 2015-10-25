package org.kaloz.pi4j.client.remote

import akka.actor.Actor.emptyBehavior
import akka.actor._

class RemoteClientActor extends Actor with ActorLogging with Configuration {

  override def receive = emptyBehavior

}

object RemoteClientActor {

  def props = Props[RemoteClientActor]
}