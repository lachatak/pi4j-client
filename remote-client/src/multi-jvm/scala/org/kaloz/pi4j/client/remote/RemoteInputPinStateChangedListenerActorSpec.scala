package org.kaloz.pi4j.client.remote

import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSubMediator.Count
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.pattern.ask
import akka.remote.testconductor.RoleName
import akka.remote.testkit.MultiNodeSpec
import akka.testkit.{EventFilter, ImplicitSender}
import akka.util.Timeout
import org.kaloz.pi4j.client.actor.PinStateChangeCallback
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue
import org.mockito.Mockito.verify
import org.scalatest.concurrent.Eventually
import org.scalatest.mock.MockitoSugar
import org.scalatest.time.{Millis, Span}

import scala.concurrent.Await
import scala.concurrent.duration._

class RemoteInputPinStateChangedListenerActorSpecMultiJvmServer extends RemoteInputPinStateChangedListenerActorSpec

class RemoteInputPinStateChangedListenerActorSpecMultiJvmClient extends RemoteInputPinStateChangedListenerActorSpec

class RemoteInputPinStateChangedListenerActorSpec extends MultiNodeSpec(RemoteClientServerConfig)
with STMultiNodeSpec with ImplicitSender with MockitoSugar with Eventually {

  import RemoteClientServerConfig._

  def initialParticipants = roles.size

  def join(from: RoleName, to: RoleName): Unit = {
    runOn(from) {
      Cluster(system) join node(to).address
    }
    enterBarrier(from.name + "-joined")
  }

  val mediator = DistributedPubSub(system).mediator

  implicit val timeout = Timeout(5 second)

  override implicit val patienceConfig = PatienceConfig(
    timeout = scaled(Span(5000, Millis)),
    interval = scaled(Span(10, Millis))
  )

  "A RemoteInputPinStateChangedListenerActor" must {

    "have 2 nodes in the cluster" in within(15 seconds) {
      join(server, server)
      join(client, server)
      enterBarrier("after-joined")
    }

    "process published message from the server" in {

      runOn(client) {
        val pinStateChangeCallback = mock[PinStateChangeCallback]
        system.actorOf(RemoteInputPinStateChangedListenerActor.props(pinStateChangeCallback), "remoteInputPinStateChangedListenerActor")

        enterBarrier("deployed")

        eventually {
          verify(pinStateChangeCallback).invoke(1, PinDigitalValue.High)
          EventFilter.debug(message = s"Listeners have been updated about pin state change --> 1 - High", occurrences = 1)
        }
        enterBarrier("verify")
      }

      runOn(server) {

        eventually {
          val count = Await.result((mediator ? Count).mapTo[Int], 5 second)
          count should be(1)
        }

        enterBarrier("deployed")

        mediator ! DistributedPubSubMediator.Publish(classOf[DigitalInputPinValueChangedEvent].getClass.getSimpleName, DigitalInputPinValueChangedEvent(1, PinDigitalValue.High))

        enterBarrier("verify")
      }

      enterBarrier("finished")
    }
  }
}