package org.kaloz.pi4j.client.messages

import com.pi4j.wiringpi.{Gpio, GpioUtil}

object ClientMessages {

  sealed trait GpioMessage

  sealed trait GpioRequest extends GpioMessage

  sealed trait GpioResponse extends GpioMessage

  sealed trait GpioCommand extends GpioMessage

  case object Done extends GpioMessage

  object GpioMessages {

    import PinMode._
    import PinValue._
    import PudMode._

    case object WiringPiSetupRequest extends GpioRequest

    case class WiringPiSetupResponse(status: Int) extends GpioResponse

    case class PinModeCommand(pin: Int, mode: PinMode) extends GpioCommand

    case class DigitalWriteCommand(pin: Int, value: PinValue) extends GpioCommand

    case class DigitalReadRequest(pin: Int) extends GpioRequest

    case class DigitalReadResponse(value: PinValue) extends GpioResponse

    case class PullUpDnControlCommand(pin: Int, pud: PudMode) extends GpioCommand

    case class PwmWriteCommand(pin: Int, value: PinValue) extends GpioCommand

  }

  object GpioUtilMessages {

    import PinDirection._
    import PinEdge._

    case class IsPinSupportedRequest(pin: Int) extends GpioRequest

    case class IsPinSupportedResponse(supported: Int) extends GpioResponse

    case class IsExportedRequest(pin: Int) extends GpioRequest

    case class IsExportedResponse(exported: Boolean) extends GpioResponse

    case class ExportCommand(pin: Int, direction: PinDirection) extends GpioCommand

    case class UnexportCommand(pin: Int) extends GpioCommand

    case class SetEdgeDetectionRequest(pin: Int, edge: PinEdge) extends GpioRequest

    case class SetEdgeDetectionResponse(status: Boolean) extends GpioResponse

    case class GetDirectionRequest(pin: Int) extends GpioRequest

    case class GetDirectionReponse(direction: PinDirection) extends GpioResponse

  }

  object GpioInterruptMessages {

    case class EnablePinStateChangeCallbackRequest(pin: Int) extends GpioRequest

    case class EnablePinStateChangeCallbackResponse(status: Int) extends GpioResponse

    case class DisablePinStateChangeCallbackRequest(pin: Int) extends GpioRequest

    case class DisablePinStateChangeCallbackResponse(status: Int) extends GpioResponse

  }

  object PinMode {

    sealed trait PinMode

    case object Input extends PinMode

    case object Output extends PinMode

    case object PwmOutput extends PinMode

    implicit def intToPinMode(pinMode: Int): PinMode = pinMode match {
      case Gpio.INPUT => Input
      case Gpio.OUTPUT => Output
      case Gpio.PWM_OUTPUT => PwmOutput
      case x: Int => throw new NotImplementedError(s"$x pinMode is not implemented!!")
    }

    implicit def pinModeToInt(pinMode: PinMode): Int = pinMode match {
      case Input => Gpio.INPUT
      case Output => Gpio.OUTPUT
      case PwmOutput => Gpio.PWM_OUTPUT
    }
  }

  object PinValue {

    sealed trait PinValue

    case object Low extends PinValue

    case object High extends PinValue

    implicit def intToPinValue(pinValue: Int): PinValue = pinValue match {
      case Gpio.LOW => Low
      case Gpio.HIGH => High
    }

    implicit def pinValueToInt(pinValue: PinValue): Int = pinValue match {
      case Low => Gpio.LOW
      case High => Gpio.HIGH
    }
  }

  object PudMode {

    sealed trait PudMode

    case object PudOff extends PudMode

    case object PudUp extends PudMode

    case object PudDown extends PudMode

    implicit def intToPudMode(pudMode: Int): PudMode = pudMode match {
      case Gpio.PUD_OFF => PudOff
      case Gpio.PUD_UP => PudUp
      case Gpio.PUD_DOWN => PudDown
      case x: Int => throw new NotImplementedError(s"$x pudMode is not implemented!!")
    }

    implicit def pudModeToInt(pudMode: PudMode): Int = pudMode match {
      case PudOff => Gpio.PUD_OFF
      case PudUp => Gpio.PUD_UP
      case PudDown => Gpio.PUD_DOWN
    }
  }

  object PinDirection {

    sealed trait PinDirection

    case object DirectionIn extends PinDirection

    case object DirectionOut extends PinDirection

    case object DirectionHigh extends PinDirection

    case object DirectionLow extends PinDirection

    implicit def intToPinDirection(pinDirection: Int): PinDirection = pinDirection match {
      case GpioUtil.DIRECTION_IN => DirectionIn
      case GpioUtil.DIRECTION_OUT => DirectionOut
      case GpioUtil.DIRECTION_HIGH => DirectionHigh
      case GpioUtil.DIRECTION_LOW => DirectionLow
      case x: Int => throw new NotImplementedError(s"$x pinDirection is not implemented!!")
    }

    implicit def pinDirectionToInt(pinDirection: PinDirection): Int = pinDirection match {
      case DirectionIn => GpioUtil.DIRECTION_IN
      case DirectionOut => GpioUtil.DIRECTION_OUT
      case DirectionHigh => GpioUtil.DIRECTION_HIGH
      case DirectionLow => GpioUtil.DIRECTION_LOW
    }
  }

  object PinEdge {

    sealed trait PinEdge

    case object EdgeNone extends PinEdge

    case object EdgeBoth extends PinEdge

    case object EdgeFalling extends PinEdge

    case object EdgeRising extends PinEdge

    implicit def intToPinEdge(pinEdge: Int): PinEdge = pinEdge match {
      case GpioUtil.EDGE_NONE => EdgeNone
      case GpioUtil.EDGE_BOTH => EdgeBoth
      case GpioUtil.EDGE_FALLING => EdgeFalling
      case GpioUtil.EDGE_RISING => EdgeRising
      case x: Int => throw new NotImplementedError(s"$x pinEdge is not implemented!!")
    }

    implicit def pinEdgeToInt(pinEdge: PinEdge): Int = pinEdge match {
      case EdgeNone => GpioUtil.EDGE_NONE
      case EdgeBoth => GpioUtil.EDGE_BOTH
      case EdgeFalling => GpioUtil.EDGE_FALLING
      case EdgeRising => GpioUtil.EDGE_RISING
    }
  }

}
