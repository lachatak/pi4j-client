package org.kaloz.pi4j.client.actor

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.pi4j.wiringpi.GpioInterrupt
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue.PinDigitalValue

abstract class InputPinStateChangedListenerActor(pinStateChangeCallback: PinStateChangeCallback) extends Actor with ActorLogging {

  override def preStart(): Unit

  override def receive: Receive = LoggingReceive {
    case DigitalInputPinValueChangedEvent(pin, value) =>
      log.info("============= ARRIVED !!")
      pinStateChangeCallback.invoke(pin, value)
      log.debug(s"Listeners have been updated about pin state change --> $pin - $value")
  }

}

trait PinStateChangeCallback {
  def invoke(pin: Int, value: PinDigitalValue)
}

class GpioInterruptPinStateChangeCallback extends PinStateChangeCallback {

  private val parameterTypes = List(classOf[Int], classOf[Boolean])
  private val pinStateChangeCallback = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  pinStateChangeCallback.setAccessible(true)

  override def invoke(pin: Int, value: PinDigitalValue): Unit = pinStateChangeCallback.invoke(null, pin: java.lang.Integer, value: java.lang.Boolean)
}
