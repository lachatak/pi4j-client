package org.kaloz.pi4j.client.remote

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSubMediator.{Count, SubscribeAck}
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import akka.remote.testconductor.RoleName
import akka.remote.testkit.MultiNodeSpec
import akka.testkit.{ImplicitSender, TestProbe}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RemoteInputPinStateChangedListenerActorSpecMultiJvmServer extends RemoteInputPinStateChangedListenerActorSpec

class RemoteInputPinStateChangedListenerActorSpecMultiJvmClient extends RemoteInputPinStateChangedListenerActorSpec

object RemoteInputPinStateChangedListenerActorSpec {

  case object Ping

  case object Pong

  class ServerActor extends Actor with ActorLogging {
    val mediator = DistributedPubSub(context.system).mediator

    def receive = {
      case Ping => mediator ! DistributedPubSubMediator.Publish("ping", Pong)
    }
  }

  class ClientActor(testProbe: ActorRef, serverActor: ActorSelection) extends Actor with ActorLogging {

    val mediator = DistributedPubSub(context.system).mediator
    mediator ! DistributedPubSubMediator.Subscribe("ping", None, self)

    override def receive = Actor.emptyBehavior

    context.become(handle())

    def handle(cancellable: Option[Cancellable] = None): Receive = {
      case Pong =>
        cancellable.foreach(_.cancel())
        context.become(handle())
        testProbe forward Pong
      case SubscribeAck(_) =>
        context.become(handle(Some(context.system.scheduler.schedule(300 millis, 300 millis) {
          serverActor ! Ping
        })))
    }
  }

}

class RemoteInputPinStateChangedListenerActorSpec extends MultiNodeSpec(RemoteClientServerConfig)
with STMultiNodeSpec with ImplicitSender {

  import RemoteInputPinStateChangedListenerActorSpec._
  import RemoteClientServerConfig._

  def initialParticipants = roles.size

  def join(from: RoleName, to: RoleName): Unit = {
    runOn(from) {
      Cluster(system) join node(to).address
      createMediator()
    }
    enterBarrier(from.name + "-joined")
  }

  def createMediator(): ActorRef = DistributedPubSub(system).mediator

  def mediator: ActorRef = DistributedPubSub(system).mediator

  def awaitCount(expected: Int): Unit = {
    awaitAssert {
      mediator ! Count
      expectMsgType[Int] should ===(expected)
    }
  }

  "A RemoteInputPinStateChangedListenerActor" must {

    "have 2 nodes in the cluster" in within(15 seconds) {
      join(server, server)
      join(client, server)
      enterBarrier("after-joined")
    }

    "send to and receive from a remote node" in {

      val testProbe = TestProbe()

      runOn(server) {
        system.actorOf(Props[ServerActor], "serverActor")

        enterBarrier("deployed")
      }

      runOn(client) {
        val serverActor = system.actorSelection(node(server) / "user" / "serverActor")
        system.actorOf(Props(classOf[ClientActor], testProbe.ref, serverActor), "clientActor")

        enterBarrier("deployed")

        testProbe.expectMsg(Pong)
      }

      enterBarrier("finished")
    }
  }
}
