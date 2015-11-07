package org.kaloz.pi4j.client.actor

import akka.actor.ActorSystem
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.DigitalInputPinValueChangedEvent
import org.scalatest.{Matchers, WordSpecLike}

class LocalInputPinStateChangedListenerActorSpec extends TestKit(ActorSystem("core-test-system", ConfigFactory.parseString(TestKitUsageSpec.config)))
with WordSpecLike
with Matchers
with ImplicitSender {

  "LocalInputPinStateChangedListenerActor" should {
    "be able to call registered listeners if InputPinStateChanged message was published to the eventstream" in {

      TestActorRef(new LocalInputPinStateChangedListenerActor)

      EventFilter.debug(message = s"Listeners have been updated about pin state change --> 1 - High", occurrences = 1) intercept {
        system.eventStream.publish(DigitalInputPinValueChangedEvent(1, java.lang.Boolean.TRUE))
      }
    }
  }
}

object TestKitUsageSpec {
  val config =
    """
      |akka {
      |  loggers = ["akka.testkit.TestEventListener"]
      |  loglevel = "DEBUG"
      |}
    """.stripMargin
}
