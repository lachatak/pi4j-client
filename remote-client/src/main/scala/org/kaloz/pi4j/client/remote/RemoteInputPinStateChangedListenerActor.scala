package org.kaloz.pi4j.client.remote

import akka.actor.Props
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import org.kaloz.pi4j.client.actor.{GpioInterruptPinStateChangeCallback, InputPinStateChangedListenerActor, PinStateChangeCallback}
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent

class RemoteInputPinStateChangedListenerActor(pinStateChangeCallback: PinStateChangeCallback) extends InputPinStateChangedListenerActor(pinStateChangeCallback) {

  override def preStart(): Unit = {
    val mediator = DistributedPubSub(context.system).mediator
    mediator ! DistributedPubSubMediator.Subscribe(classOf[DigitalInputPinValueChangedEvent].getClass.getSimpleName, self)
  }

}

object RemoteInputPinStateChangedListenerActor {

  def props(pinStateChangeCallback: PinStateChangeCallback = new GpioInterruptPinStateChangeCallback) = Props(classOf[RemoteInputPinStateChangedListenerActor], pinStateChangeCallback)
}
