package org.kaloz.pi4j.client.actor

import akka.actor.Actor.emptyBehavior
import akka.actor._
import akka.event.LoggingReceive
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.common.messages.ClientMessages.PinEdge._
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange._
import org.kaloz.pi4j.common.messages.ClientMessages.PinDigitalValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PudMode._
import org.kaloz.pi4j.common.messages.ClientMessages._

class InMemoryClientActor(pinStateChangeCallbackFactory: (ActorRefFactory, Int) => ActorRef) extends Actor with ActorLogging {

  override def receive = emptyBehavior

  context.become(handlePins())

  def handlePins(pins: Map[Int, Pin] = Map.empty[Int, Pin]): Receive = LoggingReceive {
    case WiringPiSetupRequest => sender ! WiringPiSetupResponse(0)
    case PinModeCommand(pin, mode) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(mode = mode))))
    case PullUpDnControlCommand(pin, pud) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(pud = pud))))
    case PwmWriteCommand(pin, value) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value))))
    case DigitalWriteCommand(pin, value) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value))))
      context.system.eventStream.publish(OutputPinStateChanged(pin, value))
    case DigitalReadRequest(pin) =>
      sender ! DigitalReadResponse(pins.getOrElse(pin, Pin()).value)


    case IsPinSupportedRequest(pin) => sender ! IsPinSupportedResponse(1)
    case IsExportedRequest(pin) => sender ! IsExportedResponse(pins.getOrElse(pin, Pin()).exported)
    case ExportCommand(pin, direction) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(exported = true, direction = direction))))
    case UnexportCommand(pin) =>
      context.become(handlePins(pins - pin))
    case SetEdgeDetectionRequest(pin, edge) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(edge = edge))))
      sender ! SetEdgeDetectionResponse(false)
    case GetDirectionRequest(pin) => sender ! GetDirectionResponse(pins.getOrElse(pin, Pin()).direction)


    case EnablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      val inputPinStateChangeListenerActor = pinStateChangeCallbackFactory(context, pin)
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = Some(inputPinStateChangeListenerActor)))))
      sender ! EnablePinStateChangeCallbackResponse(if (before != None) 0 else 1)
    case DisablePinStateChangeCallbackRequest(pin) =>
      val before = pins.getOrElse(pin, Pin()).enableCallback
      before.foreach(_ ! PoisonPill)
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = None))))
      sender ! DisablePinStateChangeCallbackResponse(if (before == None) 0 else 1)

    case ChangeInputPinState(pin, value) =>
      context.become(handlePins(pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value))))
      context.system.eventStream.publish(InputPinStateChanged(pin, value))

    case PinStatesRequest => sender ! PinStatesResponse(pins)
  }

}

object InMemoryClientActor {

  def props(pinStateChangeCallbackFactory: (ActorRefFactory, Int) => ActorRef) = Props(classOf[InMemoryClientActor], pinStateChangeCallbackFactory)

  object ServiceMessages {

    case object PinStatesRequest

    case class PinStatesResponse(pins: Map[Int, Pin])

    case class Pin(exported: Boolean = false,
                   direction: PinDirection = DirectionOut,
                   edge: PinEdge = EdgeNone,
                   mode: PinMode = Input,
                   pud: PudMode = PudOff,
                   value: Int = Low,
                   enableCallback: Option[ActorRef] = None)

  }

}