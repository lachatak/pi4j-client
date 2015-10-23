package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import org.kaloz.pi4j.client.GpioInterrupt
import org.kaloz.pi4j.client.messages.ClientMessages.GpioInterruptMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpioInterrupt(stubClientActor: ActorRef) extends GpioInterrupt {

  override def enablePinStateChangeCallback(pin: Int): Int = Await.result((stubClientActor ? EnablePinStateChangeCallbackRequest(pin)).mapTo[EnablePinStateChangeCallbackResponse], 1 minute).status

  override def disablePinStateChangeCallback(pin: Int): Int = Await.result((stubClientActor ? DisablePinStateChangeCallbackRequest(pin)).mapTo[DisablePinStateChangeCallbackResponse], 1 minute).status
}
