package org.kaloz.pi4j.client.remote

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.{GpioCommand, GpioRequest, GpioResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RemoteClientActor(remoteServerActor: ActorSelection) extends Actor with ActorLogging {

  implicit val timeout = Timeout(5 seconds)

  override def receive = LoggingReceive {
    case request: GpioRequest => (remoteServerActor ? request).mapTo[GpioResponse].pipeTo(sender)
    case command: GpioCommand => remoteServerActor ! command
  }

}

object RemoteClientActor {

  def props(remoteServerActor: ActorSelection) = Props(classOf[RemoteClientActor], remoteServerActor)
}