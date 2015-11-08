package org.kaloz.pi4j.client.actor

import akka.actor.Props
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent

class LocalInputPinStateChangedListenerActor(pinStateChangeCallback: PinStateChangeCallback) extends InputPinStateChangedListenerActor(pinStateChangeCallback) {

  override def preStart(): Unit = context.system.eventStream.subscribe(self, classOf[DigitalInputPinValueChangedEvent])

}

object LocalInputPinStateChangedListenerActor {

  def props(pinStateChangeCallback: PinStateChangeCallback = new GpioInterruptPinStateChangeCallback) = Props(classOf[LocalInputPinStateChangedListenerActor], pinStateChangeCallback)
}
