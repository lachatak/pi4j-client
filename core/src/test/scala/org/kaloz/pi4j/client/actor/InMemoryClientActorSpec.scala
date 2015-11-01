package org.kaloz.pi4j.client.actor

import akka.actor._
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange._
import org.kaloz.pi4j.common.messages.ClientMessages._
import org.scalatest.{Matchers, WordSpecLike}

class InMemoryClientActorSpec extends TestKit(ActorSystem("core-test-system"))
with WordSpecLike
with Matchers
with ImplicitSender {

  "InMemoryClientActor" should {
    "be able to handle WiringPiSetupRequest" in new scope {
      inMemoryClientActor ! WiringPiSetupRequest
      expectMsg(WiringPiSetupResponse(0))
    }

    "be able to handle PinModeCommand" in new scope {
      inMemoryClientActor ! PinModeCommand(2, PinMode.Input)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(2 -> Pin(mode = PinMode.Input))))
    }

    "be able to handle PullUpDnControlCommand" in new scope {
      inMemoryClientActor ! PullUpDnControlCommand(3, PudMode.PudUp)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(3 -> Pin(pud = PudMode.PudUp))))
    }

    "be able to handle PwmWriteCommand" in new scope {
      inMemoryClientActor ! PwmWriteCommand(4, 50)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(4 -> Pin(value = 50))))
    }

    "be able to handle DigitalWriteCommand" in new scope {
      inMemoryClientActor ! DigitalWriteCommand(5, PinValue.High)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(5 -> Pin(value = PinValue.High))))
    }

    "be able to handle DigitalReadRequest" in new scope {
      inMemoryClientActor ! DigitalWriteCommand(6, PinValue.High)
      inMemoryClientActor ! DigitalReadRequest(6)
      expectMsg(DigitalReadResponse(PinValue.High))
    }

    "be able to handle IsPinSupportedRequest" in new scope {
      inMemoryClientActor ! IsPinSupportedRequest(7)
      expectMsg(IsPinSupportedResponse(1))
    }

    "be able to handle IsExportedRequest" in new scope {
      inMemoryClientActor ! IsExportedRequest(8)
      expectMsg(IsExportedResponse(false))
    }

    "be able to handle ExportCommand" in new scope {
      inMemoryClientActor ! ExportCommand(9, PinDirection.DirectionHigh)
      inMemoryClientActor ! IsExportedRequest(9)
      expectMsg(IsExportedResponse(true))
    }

    "be able to handle UnexportCommand" in new scope {
      inMemoryClientActor ! ExportCommand(10, 1)
      inMemoryClientActor ! UnexportCommand(10)
      inMemoryClientActor ! IsExportedRequest(10)
      expectMsg(IsExportedResponse(false))
    }

    "be able to handle SetEdgeDetectionRequest" in new scope {
      inMemoryClientActor ! SetEdgeDetectionRequest(11, PinEdge.EdgeRising)
      expectMsg(SetEdgeDetectionResponse(false))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(11 -> Pin(edge = PinEdge.EdgeRising))))
    }

    "be able to handle GetDirectionRequest" in new scope {
      inMemoryClientActor ! ExportCommand(12, PinDirection.DirectionLow)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(12 -> Pin(exported = true, direction = PinDirection.DirectionLow))))
    }

    "be able to handle EnablePinStateChangeCallbackRequest" in new scope {
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(13)
      expectMsg(EnablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(13 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))

      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(13)
      expectMsg(EnablePinStateChangeCallbackResponse(0))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(13 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))
    }

    "be able to handle DisablePinStateChangeCallbackRequest" in new scope {
      inMemoryClientActor ! DisablePinStateChangeCallbackRequest(14)
      expectMsg(DisablePinStateChangeCallbackResponse(0))

      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(14)
      expectMsg(EnablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(14 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))

      inMemoryClientActor ! DisablePinStateChangeCallbackRequest(14)
      watcher.expectTerminated(pinStateChangeCallbackActor.ref)
      expectMsg(DisablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(14 -> Pin(enableCallback = None))))
    }

    "be able to handle ChangeInputPinState" in new scope {
      inMemoryClientActor ! ChangeInputPinState(15, PinValue.High)
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(15 -> Pin(value = 1))))
      listenerActor.underlyingActor.pin should be(15)
      listenerActor.underlyingActor.value should be(1)
    }

    "be able to handle pin chage flow" in new scope {
      inMemoryClientActor ! WiringPiSetupRequest
      inMemoryClientActor ! PinModeCommand(1, PinMode.Input)
      inMemoryClientActor ! PullUpDnControlCommand(1, PudMode.PudUp)
      inMemoryClientActor ! DigitalWriteCommand(1, PinValue.High)
      inMemoryClientActor ! ExportCommand(1, PinDirection.DirectionHigh)
      inMemoryClientActor ! SetEdgeDetectionRequest(1, PinEdge.EdgeRising)
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(1)

      inMemoryClientActor ! PinStatesRequest
      val result = fishForMessage(){
        case m:PinStatesResponse => true
        case _ => false
      }
      result should be(PinStatesResponse(Map(1 -> Pin(true, PinDirection.DirectionHigh, PinEdge.EdgeRising, PinMode.Input, PudMode.PudUp, PinValue.High, Some(pinStateChangeCallbackActor.ref)))))
    }
  }

  private trait scope {

    val pinStateChangeCallbackActor = TestProbe()
    val watcher = TestProbe()
    watcher.watch(pinStateChangeCallbackActor.ref)
    val pinStateChangeCallbackFactory: (ActorRefFactory, Int) => ActorRef = (factory, pin) => pinStateChangeCallbackActor.ref
    val inMemoryClientActor = TestActorRef(new InMemoryClientActor(pinStateChangeCallbackFactory))
    val listenerActor = TestActorRef(new ListenerActor)
  }

  class ListenerActor extends Actor {
    context.system.eventStream.subscribe(self, classOf[InputPinStateChanged])

    var pin: Int = _
    var value: Int = _

    override def receive: Receive = {
      case InputPinStateChanged(pin, value) =>
        this.pin = pin
        this.value = value
    }
  }

}
