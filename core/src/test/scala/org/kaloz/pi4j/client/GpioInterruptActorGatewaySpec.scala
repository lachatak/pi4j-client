package org.kaloz.pi4j.client

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.kaloz.pi4j.common.messages.ClientMessages.GpioInterruptMessages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GpioInterruptActorGatewaySpec extends TestKit(ActorSystem("core-test-system"))
with WordSpecLike
with Matchers {

  "GpioInterruptActorGateway" should {
    "should delegate enablePinStateChangeCallback call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(EnablePinStateChangeCallbackRequest(1))
        clientActor.reply(EnablePinStateChangeCallbackResponse(0))
      }
      gpioInterruptActorGateway.enablePinStateChangeCallback(1) should be(0)
    }

    "should delegate digitalRead call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(DisablePinStateChangeCallbackRequest(1))
        clientActor.reply(DisablePinStateChangeCallbackResponse(0))
      }
      gpioInterruptActorGateway.disablePinStateChangeCallback(1) should be(0)
    }

  }

  private trait scope {
    val clientActor = TestProbe()
    val gpioInterruptActorGateway = new GpioInterruptActorGateway(clientActor.ref)
  }

}
