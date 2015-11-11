package org.kaloz.pi4j.client.actor

import akka.actor._
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.common.messages.ClientMessages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InMemoryClientActorSpec extends TestKit(ActorSystem("core-test-system"))
with WordSpecLike
with Matchers
with ImplicitSender {

  "InMemoryClientActor" should {
    "be able to handle WiringPiSetupRequest" in new scope {
      system.eventStream.subscribe(self, WiringPiSetupEvent.getClass)
      inMemoryClientActor ! WiringPiSetupRequest
      expectMsg(WiringPiSetupEvent)
      expectMsg(WiringPiSetupResponse(0))
    }

    "be able to handle PinModeCommand" in new scope {
      system.eventStream.subscribe(self, classOf[PinModeChangedEvent])
      inMemoryClientActor ! PinModeCommand(2, PinMode.Input)
      expectMsg(PinModeChangedEvent(2, PinMode.Input))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(2 -> Pin(mode = PinMode.Input))))
    }

    "be able to handle PullUpDnControlCommand" in new scope {
      system.eventStream.subscribe(self, classOf[PullUpDnControlChangedEvent])
      inMemoryClientActor ! PullUpDnControlCommand(3, PudMode.PudUp)
      expectMsg(PullUpDnControlChangedEvent(3, PudMode.PudUp))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(3 -> Pin(pud = PudMode.PudUp))))
    }

    "be able to handle PwmWriteCommand" in new scope {
      system.eventStream.subscribe(self, classOf[PwmValueChangedEvent])
      inMemoryClientActor ! PwmWriteCommand(4, PinPwmValue(50))
      expectMsg(PwmValueChangedEvent(4, PinPwmValue(50)))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(4 -> Pin(value = PinPwmValue(50)))))
    }

    "be able to handle DigitalWriteCommand" in new scope {
      system.eventStream.subscribe(self, classOf[DigitalOutputPinValueChangedEvent])
      inMemoryClientActor ! DigitalWriteCommand(5, PinDigitalValue.High)
      expectMsg(DigitalOutputPinValueChangedEvent(5, PinDigitalValue.High))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(5 -> Pin(value = PinDigitalValue.High))))
    }

    "be able to handle DigitalReadRequest" in new scope {
      inMemoryClientActor ! DigitalWriteCommand(6, PinDigitalValue.High)
      expectMsg(DigitalOutputPinValueChangedEvent(6, PinDigitalValue.High))
      inMemoryClientActor ! DigitalReadRequest(6)
      expectMsg(DigitalReadResponse(PinDigitalValue.High))
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
      system.eventStream.subscribe(self, classOf[PinExportEvent])
      inMemoryClientActor ! ExportCommand(9, PinDirection.DirectionHigh)
      expectMsg(PinExportEvent(9, PinDirection.DirectionHigh))
      inMemoryClientActor ! IsExportedRequest(9)
      expectMsg(IsExportedResponse(true))
    }

    "be able to handle UnexportCommand" in new scope {
      system.eventStream.subscribe(self, classOf[PinUnexportEvent])
      inMemoryClientActor ! ExportCommand(10, PinDirection.DirectionHigh)
      expectMsg(PinExportEvent(10, PinDirection.DirectionHigh))
      inMemoryClientActor ! UnexportCommand(10)
      expectMsg(PinUnexportEvent(10))
      inMemoryClientActor ! IsExportedRequest(10)
      expectMsg(IsExportedResponse(false))
    }

    "be able to handle SetEdgeDetectionRequest" in new scope {
      system.eventStream.subscribe(self, classOf[EdgeDetectionChangedEvent])
      inMemoryClientActor ! SetEdgeDetectionRequest(11, PinEdge.EdgeRising)
      expectMsg(EdgeDetectionChangedEvent(11, PinEdge.EdgeRising))
      expectMsg(SetEdgeDetectionResponse(false))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(11 -> Pin(edge = PinEdge.EdgeRising))))
    }

    "be able to handle GetDirectionRequest" in new scope {
      inMemoryClientActor ! ExportCommand(12, PinDirection.DirectionLow)
      expectMsg(PinExportEvent(12, PinDirection.DirectionLow))
      inMemoryClientActor ! GetDirectionRequest(12)
      expectMsg(GetDirectionResponse(PinDirection.DirectionLow))
    }

    "be able to handle EnablePinStateChangeCallbackRequest" in new scope {
      system.eventStream.subscribe(self, classOf[PinStateChangeCallbackEnabledEvent])

      Future {
        pinStateChangeCallbackActorFactory.expectMsg(CreatePinStateChangeCallback(13,inMemoryClientActor ))
        pinStateChangeCallbackActorFactory.reply(pinStateChangeCallbackActor.ref)
      }
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(13)
      expectMsg(PinStateChangeCallbackEnabledEvent(13))
      expectMsg(EnablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(13 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))

      Future {
        pinStateChangeCallbackActorFactory.expectMsg(CreatePinStateChangeCallback(13,inMemoryClientActor ))
        pinStateChangeCallbackActorFactory.reply(pinStateChangeCallbackActor.ref)
      }
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(13)
      expectMsg(PinStateChangeCallbackEnabledEvent(13))
      expectMsg(EnablePinStateChangeCallbackResponse(0))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(13 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))
    }

    "be able to handle DisablePinStateChangeCallbackRequest" in new scope {
      system.eventStream.subscribe(self, classOf[PinStateChangeCallbackDisabledEvent])
      inMemoryClientActor ! DisablePinStateChangeCallbackRequest(14)
      expectMsg(PinStateChangeCallbackDisabledEvent(14))
      expectMsg(DisablePinStateChangeCallbackResponse(0))

      Future {
        pinStateChangeCallbackActorFactory.expectMsg(CreatePinStateChangeCallback(14,inMemoryClientActor ))
        pinStateChangeCallbackActorFactory.reply(pinStateChangeCallbackActor.ref)
      }
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(14)
      expectMsg(PinStateChangeCallbackEnabledEvent(14))
      expectMsg(EnablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(14 -> Pin(enableCallback = Some(pinStateChangeCallbackActor.ref)))))

      inMemoryClientActor ! DisablePinStateChangeCallbackRequest(14)
      watcher.expectTerminated(pinStateChangeCallbackActor.ref)
      expectMsg(PinStateChangeCallbackDisabledEvent(14))
      expectMsg(DisablePinStateChangeCallbackResponse(1))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(14 -> Pin(enableCallback = None))))
    }

    "be able to handle ChangeInputPinValue" in new scope {
      system.eventStream.subscribe(self, classOf[DigitalInputPinValueChangedEvent])
      inMemoryClientActor ! ChangeDigitalInputPinValue(15, PinDigitalValue.High)
      expectMsg(DigitalInputPinValueChangedEvent(15, PinDigitalValue.High))
      inMemoryClientActor ! PinStatesRequest
      expectMsg(PinStatesResponse(Map(15 -> Pin(value = PinDigitalValue.High))))
    }

    "be able to handle pin chage flow" in new scope {
      inMemoryClientActor ! WiringPiSetupRequest
      inMemoryClientActor ! PinModeCommand(1, PinMode.Input)
      inMemoryClientActor ! PullUpDnControlCommand(1, PudMode.PudUp)
      inMemoryClientActor ! DigitalWriteCommand(1, PinDigitalValue.High)
      inMemoryClientActor ! ExportCommand(1, PinDirection.DirectionHigh)
      inMemoryClientActor ! SetEdgeDetectionRequest(1, PinEdge.EdgeRising)
      Future {
        pinStateChangeCallbackActorFactory.expectMsg(CreatePinStateChangeCallback(1,inMemoryClientActor ))
        pinStateChangeCallbackActorFactory.reply(pinStateChangeCallbackActor.ref)
      }
      inMemoryClientActor ! EnablePinStateChangeCallbackRequest(1)

      inMemoryClientActor ! PinStatesRequest
      val result = fishForMessage() {
        case m: PinStatesResponse => true
        case _ => false
      }
      result should be(PinStatesResponse(Map(1 -> Pin(true, PinDirection.DirectionHigh, PinEdge.EdgeRising, PinMode.Input, PudMode.PudUp, PinDigitalValue.High, Some(pinStateChangeCallbackActor.ref)))))
    }
  }

  private trait scope {

    val pinStateChangeCallbackActorFactory = TestProbe()
    val pinStateChangeCallbackActor = TestProbe()
    val watcher = TestProbe()
    watcher.watch(pinStateChangeCallbackActor.ref)
    val inMemoryClientActor = TestActorRef(new InMemoryClientActor(pinStateChangeCallbackActorFactory.ref))
  }

}
