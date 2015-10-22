package org.kaloz.pi4j.client.console

import akka.actor.Actor.emptyBehavior
import akka.actor._
import org.kaloz.pi4j.client.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.client.messages.ClientMessages.PinEdge._
import org.kaloz.pi4j.client.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.client.messages.ClientMessages.PudMode._
import org.kaloz.pi4j.client.messages.ClientMessages._

class StubClientActor extends Actor with ActorLogging with Configuration {

  case class Pin(exported: Boolean = false,
                 direction: PinDirection = DirectionOut,
                 edge: PinEdge = EdgeNone,
                 mode: PinMode = Input,
                 pud: PudMode = PudOff,
                 value: PinValue = Low,
                 enableCallback: Option[ActorRef] = None)

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
    case DigitalReadRequest(pin) =>
      sender ! DigitalReadResponse(pins.getOrElse(pin, Pin()).value)


    case IsPinSupportedRequest(pin) => sender ! IsPinSupportedResponse(1)
    case IsExportedRequest(pin) => sender ! IsExportedResponse(pins.getOrElse(pin, Pin()).exported)
    case ExportCommand(pin, direction) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(exported = true, direction = direction))))
      sender ! Done

    case UnexportCommand(pin) =>
      context.become(handlePins(pins - pin))
      sender ! Done
    case SetEdgeDetectionRequest(pin, edge) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(edge = edge))))
      //verify
      sender ! SetEdgeDetectionResponse(false)
    case GetDirectionRequest(pin) => sender ! GetDirectionReponse(pins.getOrElse(pin, Pin()).direction)


    case EnablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      val pinStateChangeActor = context.system.actorOf(PinStateChangeActor.props(pin, keyMap(pin)))
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = Some(pinStateChangeActor)))))
      sender ! EnablePinStateChangeCallbackResponse(if (before != None) 0 else 1)
    case DisablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      before.foreach(_ ! PoisonPill)
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = None))))
      sender ! DisablePinStateChangeCallbackResponse(if (before == None) 0 else 1)

    case message: GpioMessage => throw new NotImplementedError(s"$message is missing!!")
  }

}

object StubClientActor {

  def props = Props[StubClientActor]
}