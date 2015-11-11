package org.kaloz.pi4j.client.console

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.jnativehook.keyboard.NativeKeyEvent
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.ChangeDigitalInputPinValue
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleInputPinStateChangeListenerActorSpec extends TestKit(ActorSystem("console-test-system"))
with WordSpecLike
with Matchers
with ImplicitSender
with OneInstancePerTest
with BeforeAndAfterAll {

  override def afterAll() = Await.ready(system.terminate(), 5 seconds)

  "ConsoleInputPinStateChangeListenerActor" should {
    "inform parent if the key was pressed" in new scope {
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyPressed(new NativeKeyEvent(2401, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.High))
      consoleInputPinStateChangeListenerActor ! PoisonPill
    }

    "not inform parent if the key has been already pressed" in new scope {
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyPressed(new NativeKeyEvent(2401, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.High))
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyPressed(new NativeKeyEvent(2401, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectNoMsg()
      consoleInputPinStateChangeListenerActor ! PoisonPill
    }

    "inform parent if the key was released" in new scope {
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyPressed(new NativeKeyEvent(2401, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.High))
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyReleased(new NativeKeyEvent(2402, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.Low))
      consoleInputPinStateChangeListenerActor ! PoisonPill
    }

    "not inform parent if the key has been already released" in new scope {
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyPressed(new NativeKeyEvent(2401, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.High))
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyReleased(new NativeKeyEvent(2402, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectMsg(ChangeDigitalInputPinValue(1, PinDigitalValue.Low))
      consoleInputPinStateChangeListenerActor.underlyingActor.nativeKeyReleased(new NativeKeyEvent(2402, System.currentTimeMillis(), 0, 97, NativeKeyEvent.VC_A, 0))
      parent.expectNoMsg()
      consoleInputPinStateChangeListenerActor ! PoisonPill
    }
  }

  private trait scope {
    val parent = TestProbe()
    val consoleInputPinStateChangeListenerActor = TestActorRef[ConsoleInputPinStateChangeListenerActor](Props(classOf[ConsoleInputPinStateChangeListenerActor], 1, 'A', parent.ref), "listener")
  }

}
