package org.kaloz.pi4j.server.remote

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.event.LoggingReceive
import com.pi4j.wiringpi.{Gpio, GpioInterrupt, GpioUtil}
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessage
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue._

import scala.util.Try

class RemoteServerActor extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[MemberRemoved])

  context.become(handle())

  override def receive: Receive = Actor.emptyBehavior

  def handle(pins: Set[Int] = Set.empty): Receive = LoggingReceive {
    case WiringPiSetupRequest => sender ! WiringPiSetupResponse(Gpio.wiringPiSetup())
    case PinModeCommand(pin, mode) => Gpio.pinMode(pin, mode)
    case PullUpDnControlCommand(pin, pud) => Gpio.pullUpDnControl(pin, pud)
    case PwmWriteCommand(pin, value) => Gpio.pwmWrite(pin, value)
    case DigitalWriteCommand(pin, value) => Gpio.digitalWrite(pin, value)
    case DigitalReadRequest(pin) => sender ! DigitalReadResponse(Gpio.digitalRead(pin))

    case IsPinSupportedRequest(pin) => sender ! IsPinSupportedResponse(GpioUtil.isPinSupported(pin))
    case IsExportedRequest(pin) => sender ! IsExportedResponse(GpioUtil.isExported(pin))
    case ExportCommand(pin, direction) =>
      context.become(handle(pins + pin))
      GpioUtil.export(pin, direction)
    case UnexportCommand(pin) =>
      context.become(handle(pins - pin))
      GpioUtil.unexport(pin)
    case SetEdgeDetectionRequest(pin, edge) => sender ! SetEdgeDetectionResponse(GpioUtil.setEdgeDetection(pin, edge))
    case GetDirectionRequest(pin) => sender ! GetDirectionResponse(GpioUtil.getDirection(pin))

    case EnablePinStateChangeCallbackRequest(pin) => sender ! EnablePinStateChangeCallbackResponse(GpioInterrupt.enablePinStateChangeCallback(pin))
    case DisablePinStateChangeCallbackRequest(pin) => sender ! DisablePinStateChangeCallbackResponse(GpioInterrupt.disablePinStateChangeCallback(pin))

    case message: DigitalInputPinValueChangedEvent => mediator ! DistributedPubSubMediator.Publish(classOf[DigitalInputPinValueChangedEvent].getClass.getSimpleName, message)

    case MemberRemoved(_, _) =>
      log.info(s"Reinitializing pins ${pins.mkString("[", ",", "]")}")
      pins.foreach { pin => Try {
        GpioInterrupt.disablePinStateChangeCallback(pin)
        GpioUtil.unexport(pin)
      }.failed.foreach(e => log.warning(s"Un-export was unsuccessful for $pin"))
      }
      context.become(handle())

    case message: GpioMessage => throw new NotImplementedError(s"$message is not handled!!")
  }
}

object RemoteServerActor {

  def props = Props[RemoteServerActor]
}
