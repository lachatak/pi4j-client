package org.kaloz.pi4j.client.console

import akka.actor.{Actor, ActorLogging, Props}
import com.pi4j.wiringpi.GpioInterrupt
import org.kaloz.pi4j.client.messages.ClientMessages.PinStateChange.PinStateChange
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue.pinValueToBoolean

class PinStateChangeListenerActor extends Actor with ActorLogging {

  //Mimic native callback
  val parameterTypes = List(classOf[Int], classOf[Boolean])
  val pinStateChangeCallback = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  pinStateChangeCallback.setAccessible(true)

  context.system.eventStream.subscribe(self, classOf[PinStateChange])

  override def receive: Receive = {
    case PinStateChange(pin, value) =>
      log.debug(s"pin $pin is set to $value")
      pinStateChangeCallback.invoke(null, pin: java.lang.Integer, value: java.lang.Boolean)
  }

}

object PinStateChangeListenerActor {

  def props = Props[PinStateChangeListenerActor]
}
