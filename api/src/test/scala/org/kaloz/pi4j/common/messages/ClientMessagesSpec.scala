package org.kaloz.pi4j.common.messages

import com.pi4j.wiringpi.{Gpio, GpioUtil}
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages.{WiringPiSetupEvent, WiringPiSetupResponse}
import org.kaloz.pi4j.common.messages.ClientMessages.PinDirection.PinDirection
import org.kaloz.pi4j.common.messages.ClientMessages.PinEdge.PinEdge
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode.PinMode
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PudMode.PudMode
import org.kaloz.pi4j.common.messages.ClientMessages._
import org.scalatest.{FunSpec, Matchers}

class ClientMessagesSpec extends FunSpec with Matchers {

  describe("GpioResponse") {
    it("should have implicit converter from GpioResponse to Option") {
      val gpioResponse: Option[GpioResponse] = WiringPiSetupResponse(0)
      gpioResponse should be(Some(WiringPiSetupResponse(0)))
    }
  }

  describe("GpioEvent") {
    it("should have implicit converter from GpioEvent to Option") {
      val gpioEvent: Option[GpioEvent] = WiringPiSetupEvent
      gpioEvent should be(Some(WiringPiSetupEvent))
    }
  }

  describe("PinMode") {
    it("should have implicit converter from Int to PinMode") {
      val inputPinMode: PinMode = Gpio.INPUT
      inputPinMode should be(PinMode.Input)

      val outputPinMode: PinMode = Gpio.OUTPUT
      outputPinMode should be(PinMode.Output)

      val pwmPinMode: PinMode = Gpio.PWM_OUTPUT
      pwmPinMode should be(PinMode.PwmOutput)

      intercept[NotImplementedError]{
        val notImplemented:PinMode = 3
      }
    }

    it("should have implicit converter from PinMode to Int") {
      val inputPinMode: Int = PinMode.Input
      inputPinMode should be(Gpio.INPUT)

      val outputPinMode: Int = PinMode.Output
      outputPinMode should be(Gpio.OUTPUT)

      val pwmPinMode: Int = PinMode.PwmOutput
      pwmPinMode should be(Gpio.PWM_OUTPUT)
    }
  }

  describe("PinValue") {
    it("should have implicit converter from Int to PinDigitalValue") {
      val lowPinValue: PinDigitalValue = Gpio.LOW
      lowPinValue should be(PinDigitalValue.Low)

      val highPinValue: PinDigitalValue = Gpio.HIGH
      highPinValue should be(PinDigitalValue.High)

      intercept[NotImplementedError]{
        val notImplemented:PinDigitalValue = 3
      }
    }

    it("should have implicit converter from PinValue to PinDigitalValue") {
      val lowPinValue: PinDigitalValue = PinDigitalValue.Low
      lowPinValue should be(PinDigitalValue.Low)

      val highPinValue: PinDigitalValue = PinDigitalValue.High
      highPinValue should be(PinDigitalValue.High)

      intercept[NotImplementedError]{
        val notImplemented:PinDigitalValue = PinValue.PinPwmValue(50)
      }
    }

    it("should have implicit converter from PinValue to Int") {
      val lowPinValue: Int = PinDigitalValue.Low
      lowPinValue should be(Gpio.LOW)

      val highPinValue: Int = PinDigitalValue.High
      highPinValue should be(Gpio.HIGH)

      val pinValue: Int = PinValue.PinPwmValue(50)
      pinValue should be(50)
    }

    it("should have implicit converter from PinDigitalValue to java.lang.Boolean") {
      val lowPinValue: java.lang.Boolean = PinDigitalValue.Low
      lowPinValue should be(java.lang.Boolean.FALSE)

      val highPinValue: java.lang.Boolean = PinDigitalValue.High
      highPinValue should be(java.lang.Boolean.TRUE)
    }

    it("should have implicit converter from java.lang.Boolean to PinDigitalValue") {
      val lowPinValue: PinDigitalValue = java.lang.Boolean.FALSE
      lowPinValue should be(PinDigitalValue.Low)

      val highPinValue: PinDigitalValue = java.lang.Boolean.TRUE
      highPinValue should be(PinDigitalValue.High)
    }
  }

  describe("PudMode") {
    it("should have implicit converter from Int to PudMode") {
      val offPudMode: PudMode = Gpio.PUD_OFF
      offPudMode should be(PudMode.PudOff)

      val downPudMode: PudMode = Gpio.PUD_DOWN
      downPudMode should be(PudMode.PudDown)

      val upPudMode: PudMode = Gpio.PUD_UP
      upPudMode should be(PudMode.PudUp)

      intercept[NotImplementedError]{
        val notImplemented:PudMode = 3
      }
    }

    it("should have implicit converter from PudMode to Int") {
      val offPudMode: Int = PudMode.PudOff
      offPudMode should be(Gpio.PUD_OFF)

      val downPudMode: Int = PudMode.PudDown
      downPudMode should be(Gpio.PUD_DOWN)

      val upPudMode: Int = PudMode.PudUp
      upPudMode should be(Gpio.PUD_UP)
    }
  }

  describe("PinDirection") {
    it("should have implicit converter from Int to PinDirection") {
      val directionIn: PinDirection = GpioUtil.DIRECTION_IN
      directionIn should be(PinDirection.DirectionIn)

      val directionOut: PinDirection = GpioUtil.DIRECTION_OUT
      directionOut should be(PinDirection.DirectionOut)

      val directionHigh: PinDirection = GpioUtil.DIRECTION_HIGH
      directionHigh should be(PinDirection.DirectionHigh)

      val directionLow: PinDirection = GpioUtil.DIRECTION_LOW
      directionLow should be(PinDirection.DirectionLow)

      intercept[NotImplementedError]{
        val notImplemented:PinDirection = 4
      }
    }

    it("should have implicit converter from PinDirection to Int") {
      val directionIn: Int = PinDirection.DirectionIn
      directionIn should be(GpioUtil.DIRECTION_IN)

      val directionOut: Int = PinDirection.DirectionOut
      directionOut should be(GpioUtil.DIRECTION_OUT)

      val directionHigh: Int = PinDirection.DirectionHigh
      directionHigh should be(GpioUtil.DIRECTION_HIGH)

      val directionLow: Int = PinDirection.DirectionLow
      directionLow should be(GpioUtil.DIRECTION_LOW)
    }
  }

  describe("PinEdge") {
    it("should have implicit converter from Int to PinEdge") {
      val none: PinEdge = GpioUtil.EDGE_NONE
      none should be(PinEdge.EdgeNone)

      val both: PinEdge = GpioUtil.EDGE_BOTH
      both should be(PinEdge.EdgeBoth)

      val falling: PinEdge = GpioUtil.EDGE_FALLING
      falling should be(PinEdge.EdgeFalling)

      val rising: PinEdge = GpioUtil.EDGE_RISING
      rising should be(PinEdge.EdgeRising)

      intercept[NotImplementedError]{
        val notImplemented:PinEdge = 4
      }
    }

    it("should have implicit converter from PinEdge to Int") {
      val none: Int = PinEdge.EdgeNone
      none should be(GpioUtil.EDGE_NONE)

      val both: Int = PinEdge.EdgeBoth
      both should be(GpioUtil.EDGE_BOTH)

      val falling: Int = PinEdge.EdgeFalling
      falling should be(GpioUtil.EDGE_FALLING)

      val rising: Int = PinEdge.EdgeRising
      rising should be(GpioUtil.EDGE_RISING)
    }
  }
}
