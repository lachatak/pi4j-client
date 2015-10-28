package org.kaloz.pi4j.client.aspect

import org.kaloz.pi4j.client
import org.mockito.Mockito._
import org.scalatest.{FunSpec, Matchers}

class GpioInterruptAspectSpec extends FunSpec with Matchers {

  describe("GpioInterruptAspect") {
    it("should weave GpioInterrupts calls and delegate call to the client instead") {

      when(MockClientFactory.instance.gpioInterrupt.enablePinStateChangeCallback(1)).thenReturn(2)
      GpioInterruptWrapper.enablePinStateChangeCallback(1) should be(2)

      when(MockClientFactory.instance.gpioInterrupt.disablePinStateChangeCallback(1)).thenReturn(2)
      GpioInterruptWrapper.disablePinStateChangeCallback(1) should be(2)
    }
  }
}


object GpioInterruptWrapper extends client.GpioInterrupt {

  import com.pi4j.wiringpi.{GpioInterrupt => JGpioInterrupt}

  override def enablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.enablePinStateChangeCallback(pin)

  override def disablePinStateChangeCallback(pin: Int): Int = JGpioInterrupt.disablePinStateChangeCallback(pin)
}