package org.kaloz.pi4j.client.actor

import akka.actor.Actor.emptyBehavior
import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.{CreatePinStateChangeCallback, PinStates}
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.common.messages.ClientMessages.PinEdge._
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PudMode._
import org.kaloz.pi4j.common.messages.ClientMessages.{GpioEvent, GpioResponse}

import scala.concurrent.Await
import scala.concurrent.duration._

class InMemoryClientActor(pinStateChangeListenerActorFactory: ActorRef) extends Actor with ActorLogging {

  override def receive = emptyBehavior

  context.become(handlePins())

  def handlePins(pins: PinStates = Map.empty[Int, Pin]): Receive = LoggingReceive {

    def pinStateChanged(update: PinStates => PinStates = identity, event: Option[GpioEvent] = None, response: Option[GpioResponse] = None): Unit = {
      context.become(handlePins(update(pins)))
      event.foreach(context.system.eventStream.publish(_))
      response.foreach(sender ! _)
    }

    {
      case WiringPiSetupRequest =>
        pinStateChanged(event = WiringPiSetupEvent, response = WiringPiSetupResponse(0))
      case PinModeCommand(pin, mode) =>
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(mode = mode)), PinModeChangedEvent(pin, mode))
      case PullUpDnControlCommand(pin, pud) =>
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(pud = pud)), PullUpDnControlChangedEvent(pin, pud))
      case PwmWriteCommand(pin, value) =>
        log.info(s"=====>>>>> PWM pin $pin value has changed --> $value")
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value)), PwmValueChangedEvent(pin, value))
      case DigitalWriteCommand(pin, value) =>
        log.info(s"----->>>>> digital OUTPUT pin $pin value has changed $value")
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value)), DigitalOutputPinValueChangedEvent(pin, value))
      case DigitalReadRequest(pin) =>
        sender ! DigitalReadResponse(pins.getOrElse(pin, Pin()).value)


      case IsPinSupportedRequest(pin) => sender ! IsPinSupportedResponse(1)
      case IsExportedRequest(pin) => sender ! IsExportedResponse(pins.getOrElse(pin, Pin()).exported)
      case ExportCommand(pin, direction) =>
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(exported = true, direction = direction)), PinExportEvent(pin, direction))
      case UnexportCommand(pin) =>
        pinStateChanged(_ - pin, PinUnexportEvent(pin))
      case SetEdgeDetectionRequest(pin, edge) =>
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(edge = edge)),
          EdgeDetectionChangedEvent(pin, edge), SetEdgeDetectionResponse(false))
      case GetDirectionRequest(pin) => sender ! GetDirectionResponse(pins.getOrElse(pin, Pin()).direction)


      case EnablePinStateChangeCallbackRequest(pin) =>
        implicit val timeout = Timeout(5 seconds)
        val before = pins.getOrElse(pin, Pin()).enableCallback
        val inputPinStateChangeListenerActor = Await.result((pinStateChangeListenerActorFactory ? CreatePinStateChangeCallback(pin, self)).mapTo[ActorRef], 5 seconds)
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = Some(inputPinStateChangeListenerActor))),
          PinStateChangeCallbackEnabledEvent(pin), EnablePinStateChangeCallbackResponse(if (before != None) 0 else 1))
      case DisablePinStateChangeCallbackRequest(pin) =>
        val before = pins.getOrElse(pin, Pin()).enableCallback
        before.foreach(_ ! PoisonPill)
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(enableCallback = None)),
          PinStateChangeCallbackDisabledEvent(pin), DisablePinStateChangeCallbackResponse(if (before == None) 0 else 1))

      case ChangeDigitalInputPinValue(pin, value) =>
        log.info(s"<<<<<<<----- digital INPUT pin $pin value has changed <-- $value")
        pinStateChanged(pins => pins + (pin -> pins.getOrElse(pin, Pin()).copy(value = value)), DigitalInputPinValueChangedEvent(pin, value))

      case PinStatesRequest => sender ! PinStatesResponse(pins)
    }
  }

}

object InMemoryClientActor {

  type PinStates = Map[Int, Pin]


  def props(pinStateChangeListenerActorFactory: ActorRef) = Props(classOf[InMemoryClientActor], pinStateChangeListenerActorFactory)

  case class CreatePinStateChangeCallback(pin: Int, sender: ActorRef)

  object ServiceMessages {

    case object PinStatesRequest

    case class PinStatesResponse(pins: PinStates)

    case class Pin(exported: Boolean = false,
                   direction: PinDirection = DirectionOut,
                   edge: PinEdge = EdgeNone,
                   mode: PinMode = Input,
                   pud: PudMode = PudOff,
                   value: PinValue = Low,
                   enableCallback: Option[ActorRef] = None)

  }

}