package org.kaloz.pi4j.client.remote

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.event.LoggingReceive
import com.pi4j.wiringpi.GpioInterrupt
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.InputPinStateChanged
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.pinValueToBoolean

//TODO extract this to a parent trait along side with console input listener. preStart is the only difference between this 2 actors
class InputPinStateChangedListenerActor extends Actor with ActorLogging {

  //Handle native callback from remote server
  val parameterTypes = List(classOf[Int], classOf[Boolean])
  val pinStateChangeCallback = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  pinStateChangeCallback.setAccessible(true)

  override def preStart(): Unit = {
    val mediator = DistributedPubSub(context.system).mediator
    mediator ! DistributedPubSubMediator.Subscribe(classOf[InputPinStateChanged].getClass.getSimpleName, self)
  }

  override def receive: Receive = LoggingReceive {
    case InputPinStateChanged(pin, value) => pinStateChangeCallback.invoke(null, pin: java.lang.Integer, value: java.lang.Boolean)
  }

}

object InputPinStateChangedListenerActor {

  def props = Props[InputPinStateChangedListenerActor]
}
