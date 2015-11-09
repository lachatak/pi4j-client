package org.kaloz.pi4j.server.remote

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSubMediator.Count
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.pattern.ask
import akka.remote.testconductor.RoleName
import akka.remote.testkit.MultiNodeSpec
import akka.testkit.ImplicitSender
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}

import scala.concurrent.Await
import scala.concurrent.duration._

class RemoteServerActorSpecMultiJvmServer extends RemoteServerActorSpec

class RemoteServerActorSpecMultiJvmClient extends RemoteServerActorSpec

class RemoteServerActorSpec extends MultiNodeSpec(RemoteClientServerConfig)
with STMultiNodeSpec with ImplicitSender with Eventually {

  import RemoteClientServerConfig._

  def initialParticipants = roles.size

  def join(from: RoleName, to: RoleName): Unit = {
    runOn(from) {
      Cluster(system) join node(to).address
    }
    enterBarrier(from.name + "-joined")
  }

  val mediator = DistributedPubSub(system).mediator
  mediator ! DistributedPubSubMediator.Subscribe(classOf[DigitalInputPinValueChangedEvent].getClass.getSimpleName, self)

  implicit val timeout = Timeout(5 second)

  "A RemoteServerActor test" must {

    "have 2 nodes in the cluster" in within(15 seconds) {
      join(server, server)
      join(client, server)
      enterBarrier("after-joined")
    }

    "publish DigitalInputPinValueChangedEvent message to the cluster" in {

      runOn(client) {

        fishForMessage(Duration(10, TimeUnit.SECONDS)) {
          case DigitalInputPinValueChangedEvent(1, PinDigitalValue.High) => true
          case _ => false
        }

        enterBarrier("verify")
      }

      runOn(server) {

        val remoteServer = system.actorOf(Props[RemoteServerActor], "remoteServer")

        implicit val patienceConfig = PatienceConfig(
          timeout = scaled(Span(5000, Millis)),
          interval = scaled(Span(10, Millis))
        )

        eventually {
          Await.result((mediator ? Count).mapTo[Int], 5 second) should be(2)
        }

        remoteServer ! DigitalInputPinValueChangedEvent(1, PinDigitalValue.High)

        enterBarrier("verify")
      }

      enterBarrier("finished")
    }
  }
}