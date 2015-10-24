package org.kaloz.pi4j.client.fallback

import com.pi4j.wiringpi.{GpioInterrupt => JGpioInterrupt}
import org.kaloz.pi4j.client.GpioInterrupt

class FallbackGpioInterrupt extends GpioInterrupt {

  override def enablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.enablePinStateChangeCallback(pin)

  override def disablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.disablePinStateChangeCallback(pin)
}
