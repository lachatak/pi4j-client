package org.kaloz.pi4j.client.remote


import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages.{PinModeCommand, WiringPiSetupResponse, WiringPiSetupRequest}
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode
import org.scalatest.{Matchers, WordSpecLike}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import akka.pattern.ask
import scala.concurrent.duration._

class RemoteClientActorSpec extends TestKit(ActorSystem("remote-test-system"))
with WordSpecLike
with Matchers
with ImplicitSender {

  implicit val timeout = Timeout(5 seconds)

  "RemoteClientActor" should {
    "forward incoming requests and feed back responses from the target" in new scope {
      Future {
        remoteServerActor.expectMsg(WiringPiSetupRequest)
        remoteServerActor.reply(WiringPiSetupResponse(1))
      }
      Await.result((remoteClientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 5 seconds) should be(WiringPiSetupResponse(1))
    }

    "forward incoming command fire and forget way to the target" in new scope {
      remoteClientActor ! PinModeCommand(1, PinMode.Input)
      remoteServerActor.expectMsg(PinModeCommand(1, PinMode.Input))
    }
  }

  private trait scope {
    val remoteServerActor = TestProbe("remoteServerActor")
    val remoteServerActorSelection = system.actorSelection(remoteServerActor.ref.path)
    val remoteClientActor = TestActorRef(new RemoteClientActor(remoteServerActorSelection))

  }

}
