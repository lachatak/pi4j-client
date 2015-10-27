package org.kaloz.pi4j.client

trait GpioInterrupt {

  def enablePinStateChangeCallback(pin: Int): Int = ???

  def disablePinStateChangeCallback(pin: Int): Int = ???
}
