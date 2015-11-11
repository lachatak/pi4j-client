package org.kaloz.pi4j.client.console

import akka.actor._
import akka.testkit._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback
import org.mockito.Mockito
import org.scalatest._
import org.scalatest.mock.MockitoSugar

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleInputPinStateChangeListenerActorFactorySpec extends TestKit(ActorSystem("console-test-system", ConfigFactory.load("pi4j-console-client")))
with WordSpecLike
with Matchers
with ImplicitSender
with OneInstancePerTest
with BeforeAndAfterAll
with MockitoSugar {

  override def afterAll() = Await.ready(system.terminate(), 5 seconds)

  "ConsoleInputPinStateChangeListenerActorFactory" should {
    "create a new ConsoleInputPinStateChangeListenerActor when a CreatePinStateChangeCallback message is processed" in new scope {
      consoleInputPinStateChangeListenerActorFactory ! CreatePinStateChangeCallback(1, self)
      expectMsg(testProbe.ref)
    }

    "remove actor if it is terminated" in new scope {
      consoleInputPinStateChangeListenerActorFactory ! CreatePinStateChangeCallback(1, self)
      expectMsg(testProbe.ref)

      println(testProbe.ref)
      EventFilter.debug(message = s"Listener actor deleted from the registry ${testProbe.ref}", occurrences = 1) intercept {
        testProbe.ref ! PoisonPill
      }
    }
  }

  private trait scope {
    val actorRefFactory = mock[ActorRefFactory]
    val testProbe = TestProbe()
    implicit val timeout = Timeout(5 second)
    Mockito.when(actorRefFactory.actorOf(Props(classOf[ConsoleInputPinStateChangeListenerActor], 1, 'A', self), s"console-1-A-actor")).thenReturn(testProbe.ref)
    val consoleInputPinStateChangeListenerActorFactory = TestActorRef(new ConsoleInputPinStateChangeListenerActorFactory(actorRefFactory), "factory")
  }

}