package org.kaloz.pi4j.client.remote

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.remote.testconductor.RoleName
import akka.remote.testkit.MultiNodeSpec
import akka.testkit.{EventFilter, ImplicitSender}
import org.kaloz.pi4j.common.messages.ClientMessages.PinDigitalValue
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.InputPinStateChanged

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RemoteInputPinStateChangedListenerActorSpecMultiJvmServer extends RemoteInputPinStateChangedListenerActorSpec

class RemoteInputPinStateChangedListenerActorSpecMultiJvmClient extends RemoteInputPinStateChangedListenerActorSpec

object RemoteInputPinStateChangedListenerActorSpec {

  case object Ping

  class RemoteServer extends Actor with ActorLogging {
    val mediator = DistributedPubSub(context.system).mediator

    def receive = {
      case Ping =>
        context.system.scheduler.scheduleOnce(2 seconds) {
          mediator ! DistributedPubSubMediator.Publish(classOf[InputPinStateChanged].getClass.getSimpleName, InputPinStateChanged(1, PinDigitalValue.High))
        }
    }
  }

}

class RemoteInputPinStateChangedListenerActorSpec extends MultiNodeSpec(RemoteClientServerConfig)
with STMultiNodeSpec with ImplicitSender {

  import RemoteClientServerConfig._
  import RemoteInputPinStateChangedListenerActorSpec._

  def initialParticipants = roles.size

  def join(from: RoleName, to: RoleName): Unit = {
    runOn(from) {
      Cluster(system) join node(to).address
    }
    enterBarrier(from.name + "-joined")
  }

  "A RemoteInputPinStateChangedListenerActor" must {

    "have 2 nodes in the cluster" in within(15 seconds) {
      join(server, server)
      join(client, server)
      enterBarrier("after-joined")
    }

    "process published message from the server" in {

      runOn(server) {
        system.actorOf(Props[RemoteServer], "remoteServer")

        enterBarrier("deployed")
      }

      runOn(client) {
        val serverActor = system.actorSelection(node(server) / "user" / "remoteServer")
        system.actorOf(Props(classOf[RemoteInputPinStateChangedListenerActor]), "remoteInputPinStateChangedListenerActor")

        enterBarrier("deployed")

        EventFilter.debug(message = s"Listeners have been updated about pin state change --> 1 - High", occurrences = 1) intercept {
          serverActor ! Ping
        }
      }

      enterBarrier("finished")
    }
  }
}
