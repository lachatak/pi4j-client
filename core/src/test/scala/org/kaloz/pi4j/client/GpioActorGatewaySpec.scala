package org.kaloz.pi4j.client

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.pi4j.wiringpi.{Gpio => JGpio}
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GpioActorGatewaySpec extends TestKit(ActorSystem("core-test-system"))
with WordSpecLike
with Matchers {

  "GpioActorGateway" should {
    "should delegate wiringPiSetup call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(WiringPiSetupRequest)
        clientActor.reply(WiringPiSetupResponse(0))
      }
      gpioActorGateway.wiringPiSetup should be(0)
    }

    "should delegate pinMode call to the underlying actor" in new scope {
      gpioActorGateway.pinMode(1, JGpio.INPUT)
      clientActor.expectMsg(PinModeCommand(1, PinMode.Input))
    }


    "should delegate pullUpDnControl call to the underlying actor" in new scope {
      gpioActorGateway.pullUpDnControl(1, JGpio.PUD_UP)
      clientActor.expectMsg(PullUpDnControlCommand(1, PudMode.PudUp))
    }

    "should delegate digitalWrite call to the underlying actor" in new scope {
      gpioActorGateway.digitalWrite(1, JGpio.HIGH)
      clientActor.expectMsg(DigitalWriteCommand(1, PinDigitalValue.High))
    }

    "should delegate digitalRead call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(DigitalReadRequest(1))
        clientActor.reply(DigitalReadResponse(PinDigitalValue.High))
      }
      gpioActorGateway.digitalRead(1) should be(JGpio.HIGH)
    }

    "should delegate pwmWrite call to the underlying actor" in new scope {
      gpioActorGateway.pwmWrite(1, 50)
      clientActor.expectMsg(PwmWriteCommand(1, 50))
    }
  }

  private trait scope {
    val clientActor = TestProbe()
    val gpioActorGateway = new GpioActorGateway(clientActor.ref)
  }

}
