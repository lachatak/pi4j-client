package org.kaloz.pi4j.client.aspect

import org.kaloz.pi4j.client
import org.kaloz.pi4j.client.factory.AbstractClientFactory
import org.mockito.Mockito._
import org.scalatest.{FunSpec, Matchers}

class GpioAspectSpec extends FunSpec with Matchers {

  describe("GpioAspect") {
    it("should weave Gpio calls and delegate call to the client instead") {

      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, this.getClass.getPackage.getName)

      when(MockClientFactory.instance.gpio.wiringPiSetup).thenReturn(1)
      GpioWrapper.wiringPiSetup should be(1)

      GpioWrapper.pinMode(1, 2)
      verify(MockClientFactory.instance.gpio).pinMode(1, 2)

      GpioWrapper.pullUpDnControl(1, 2)
      verify(MockClientFactory.instance.gpio).pullUpDnControl(1, 2)

      GpioWrapper.digitalWrite(1, 2)
      verify(MockClientFactory.instance.gpio).digitalWrite(1, 2)

      GpioWrapper.pwmWrite(1, 2)
      verify(MockClientFactory.instance.gpio).pwmWrite(1, 2)

      when(MockClientFactory.instance.gpio.digitalRead(1)).thenReturn(2)
      GpioWrapper.digitalRead(1) should be(2)

    }
  }
}

object GpioWrapper extends client.Gpio {

  import com.pi4j.wiringpi.{Gpio => JGpio}

  override def wiringPiSetup: Int = JGpio.wiringPiSetup

  override def pinMode(pin: Int, mode: Int): Unit = JGpio.pinMode(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = JGpio.pullUpDnControl(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = JGpio.digitalWrite(pin, value)

  override def pwmWrite(pin: Int, value: Int): Unit = JGpio.pwmWrite(pin, value)

  override def digitalRead(pin: Int): Int = JGpio.digitalRead(pin)
}