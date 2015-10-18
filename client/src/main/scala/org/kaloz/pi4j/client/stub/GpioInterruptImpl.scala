package org.kaloz.pi4j.client.stub

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.GpioInterrupt
import org.kaloz.pi4j.client.common.Pi4jClientMessages.GpioInterruptMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioInterruptImpl(stubClientActor: ActorRef) extends GpioInterrupt {

  implicit val timeout = Timeout(1 minutes)

  def enablePinStateChangeCallback(pin: Int): Int = Await.result((stubClientActor ? EnablePinStateChangeCallbackRequest(pin)).mapTo[EnablePinStateChangeCallbackResponse], 1 minute).status

  def disablePinStateChangeCallback(pin: Int): Int = Await.result((stubClientActor ? DisablePinStateChangeCallbackRequest(pin)).mapTo[DisablePinStateChangeCallbackResponse], 1 minute).status
}
