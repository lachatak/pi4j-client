package org.kaloz.pi4j.client.remote

import akka.actor.Props
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import org.kaloz.pi4j.client.actor.InputPinStateChangedListenerActor
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent

class RemoteInputPinStateChangedListenerActor extends InputPinStateChangedListenerActor {

  override def preStart(): Unit = {
    val mediator = DistributedPubSub(context.system).mediator
    mediator ! DistributedPubSubMediator.Subscribe(classOf[DigitalInputPinValueChangedEvent].getClass.getSimpleName, self)
  }

}

object RemoteInputPinStateChangedListenerActor {

  def props = Props[RemoteInputPinStateChangedListenerActor]
}
