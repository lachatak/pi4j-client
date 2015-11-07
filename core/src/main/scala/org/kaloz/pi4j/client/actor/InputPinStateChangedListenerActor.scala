package org.kaloz.pi4j.client.actor

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.pi4j.wiringpi.GpioInterrupt
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.pinValueToBoolean

trait InputPinStateChangedListenerActor extends Actor with ActorLogging {

  //TODO extract it to a separate trait
  val parameterTypes = List(classOf[Int], classOf[Boolean])
  val pinStateChangeCallback = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  pinStateChangeCallback.setAccessible(true)

  override def preStart(): Unit

  override def receive: Receive = LoggingReceive {
    case DigitalInputPinValueChangedEvent(pin, value) =>
      pinStateChangeCallback.invoke(null, pin: java.lang.Integer, value: java.lang.Boolean)
      log.debug(s"Listeners have been updated about pin state change --> $pin - $value")
  }

}
