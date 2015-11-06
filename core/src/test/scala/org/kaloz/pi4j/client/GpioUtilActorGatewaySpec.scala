package org.kaloz.pi4j.client

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.pi4j.wiringpi.{GpioUtil => JGpioUtil}
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GpioUtilActorGatewaySpec extends TestKit(ActorSystem("core-test-system"))
with WordSpecLike
with Matchers {

  "GpioUtilActorGateway" should {
    "should delegate export call to the underlying actor" in new scope {
      gpioUtilActorGateway.export(1, JGpioUtil.DIRECTION_IN)
      clientActor.expectMsg(ExportCommand(1, PinDirection.DirectionIn))
    }

    "should delegate unexport call to the underlying actor" in new scope {
      gpioUtilActorGateway.unexport(1)
      clientActor.expectMsg(UnexportCommand(1))
    }

    "should delegate isExported call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(IsExportedRequest(1))
        clientActor.reply(IsExportedResponse(true))
      }
      gpioUtilActorGateway.isExported(1) should be(true)
    }

    "should delegate setEdgeDetection call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(SetEdgeDetectionRequest(1, JGpioUtil.EDGE_FALLING))
        clientActor.reply(SetEdgeDetectionResponse(true))
      }
      gpioUtilActorGateway.setEdgeDetection(1, JGpioUtil.EDGE_FALLING) should be(true)
    }

    "should delegate getDirection call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(GetDirectionRequest(1))
        clientActor.reply(GetDirectionResponse(PinDirection.DirectionLow))
      }
      gpioUtilActorGateway.getDirection(1) should be(JGpioUtil.DIRECTION_LOW)
    }

    "should delegate isPinSupported call to the underlying actor and return the response" in new scope {
      Future {
        clientActor.expectMsg(IsPinSupportedRequest(1))
        clientActor.reply(IsPinSupportedResponse(1))
      }
      gpioUtilActorGateway.isPinSupported(1) should be(1)
    }
  }

  private trait scope {
    val clientActor = TestProbe()
    val gpioUtilActorGateway = new GpioUtilActorGateway(clientActor.ref)
  }

}
