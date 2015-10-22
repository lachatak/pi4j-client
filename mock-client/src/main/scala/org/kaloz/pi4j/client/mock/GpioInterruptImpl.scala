package org.kaloz.pi4j.client.mock

import com.pi4j.wiringpi.{GpioInterrupt => JGpioInterrupt}
import org.kaloz.pi4j.client.GpioInterrupt

class GpioInterruptImpl extends GpioInterrupt {

  def enablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.enablePinStateChangeCallback(pin)

  def disablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.disablePinStateChangeCallback(pin)
}
