package org.kaloz.pi4j.client

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioInterruptActorGateway(clientActor: ActorRef) extends GpioInterrupt {

  implicit val timeout = Timeout(5 seconds)

  override def enablePinStateChangeCallback(pin: Int): Int = Await.result((clientActor ? EnablePinStateChangeCallbackRequest(pin)).mapTo[EnablePinStateChangeCallbackResponse], 5 seconds).status

  override def disablePinStateChangeCallback(pin: Int): Int = Await.result((clientActor ? DisablePinStateChangeCallbackRequest(pin)).mapTo[DisablePinStateChangeCallbackResponse], 5 seconds).status
}
