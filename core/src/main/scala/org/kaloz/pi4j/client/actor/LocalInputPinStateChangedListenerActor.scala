package org.kaloz.pi4j.client.actor

import akka.actor.Props
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent

class LocalInputPinStateChangedListenerActor extends InputPinStateChangedListenerActor {

  override def preStart(): Unit = context.system.eventStream.subscribe(self, classOf[DigitalInputPinValueChangedEvent])

}

object LocalInputPinStateChangedListenerActor {

  def props = Props[LocalInputPinStateChangedListenerActor]
}
