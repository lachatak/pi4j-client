package org.kaloz.pi4j.client.console

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.LoggingReceive
import com.pi4j.wiringpi.GpioInterrupt
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.InputPinStateChanged
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.pinValueToBoolean

class InputPinStateChangedListenerActor extends Actor with ActorLogging {

  //Mimic native callback
  val parameterTypes = List(classOf[Int], classOf[Boolean])
  val pinStateChangeCallback = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  pinStateChangeCallback.setAccessible(true)

  override def preStart(): Unit = context.system.eventStream.subscribe(self, classOf[InputPinStateChanged])

  override def receive: Receive = LoggingReceive {
    case InputPinStateChanged(pin, value) => pinStateChangeCallback.invoke(null, pin: java.lang.Integer, value: java.lang.Boolean)
  }

}

object InputPinStateChangedListenerActor {

  def props = Props[InputPinStateChangedListenerActor]
}
