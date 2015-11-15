package org.kaloz.pi4j.client.web

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._

class WebInputPinStateChangeListenerActorFactorySpec
  extends TestKit(ActorSystem("web-test-system"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  override def afterAll() = Await.ready(system.terminate(), 5 seconds)

  "WebInputPinStateChangeListenerActorFactory" should {
    "send the same actor ref ever the time" in new scope {
      webInputPinStateChangeListenerActorFactory ! CreatePinStateChangeCallback(anyInt, anyParent)
      expectMsg(testActorRef)
    }
  }

  private trait scope {
    val testActorRef = TestProbe().ref
    val anyInt = 0
    val anyParent = TestProbe().ref
    val webInputPinStateChangeListenerActorFactory = TestActorRef(WebInputPinStateChangeListenerActorFactory.props(testActorRef))
  }
}
