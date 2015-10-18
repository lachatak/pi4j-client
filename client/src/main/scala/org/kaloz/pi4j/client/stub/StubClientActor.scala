package org.kaloz.pi4j.client.stub

import akka.actor.Actor.emptyBehavior
import akka.actor.{Actor, ActorLogging, Props}
import org.kaloz.pi4j.client.common.Pi4jClientMessages._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinDirection._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinEdge._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinValue._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinMode._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PudMode._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.GpioMessages._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.GpioInterruptMessages._

class StubClientActor extends Actor with ActorLogging {

  case class Pin(exported: Boolean = false,
                 direction: PinDirection = DirectionOut,
                 edge: PinEdge = None,
                 mode: PinMode = Input,
                 pud: PudMode = Off,
                 value: PinValue = Low,
                 enableCallback: Boolean = false)

  override def receive = emptyBehavior

  context.become(handlePins())

  def logging(pins: Map[Int, Pin] = Map.empty[Int, Pin]): PartialFunction[Any, Any] = {
    case message: GpioMessage =>
      log.info(s"Message received $message!")
      message
    case message => message
  }

  def handlePins(pins: Map[Int, Pin] = Map.empty[Int, Pin]): Receive = logging(pins) andThen handle(pins)

  def handle(pins: Map[Int, Pin] = Map.empty[Int, Pin]): Receive = {
    case WiringPiSetupRequest => sender ! WiringPiSetupResponse(0)
    case PinModeCommand(pin, mode) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(mode = mode))))
      sender ! Done
    case PullUpDnControlCommand(pin, pud) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(pud = pud))))
      sender ! Done
    case PwmWriteCommand(pin, value) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value))))
      sender ! Done
    case DigitalWriteCommand(pin, value) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value))))
      sender ! Done

    case IsPinSupportedRequest(pin) => sender ! IsPinSupportedResponse(1)
    case IsExportedRequest(pin) => sender ! IsExportedResponse(pins.getOrElse(pin, Pin()).exported)
    case ExportCommand(pin, direction) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(exported = true, direction = direction))))
      sender ! Done
    case UnexportCommand(pin) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(exported = false))))
      sender ! Done
    case SetEdgeDetectionRequest(pin, edge) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(edge = edge))))
      //verify
      sender ! SetEdgeDetectionResponse(false)
    case GetDirectionRequest(pin) => sender ! GetDirectionReponse(pins.getOrElse(pin, Pin()).direction)

    case EnablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = true))))
      sender ! EnablePinStateChangeCallbackResponse(if (before == true) 0 else 1)
    case DisablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = false))))
      sender ! DisablePinStateChangeCallbackResponse(if (before == false) 0 else 1)

    case message: GpioMessage => throw new NotImplementedError(s"$message is missing!!")
  }

}

object StubClientActor {

  def props = Props[StubClientActor]
}