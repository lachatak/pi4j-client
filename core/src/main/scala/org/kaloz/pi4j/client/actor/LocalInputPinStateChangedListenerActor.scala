package org.kaloz.pi4j.client.actor

import akka.actor.Props
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.InputPinStateChanged

class LocalInputPinStateChangedListenerActor extends InputPinStateChangedListenerActor {

  override def preStart(): Unit = context.system.eventStream.subscribe(self, classOf[InputPinStateChanged])

}

object LocalInputPinStateChangedListenerActor {

  def props = Props[LocalInputPinStateChangedListenerActor]
}
