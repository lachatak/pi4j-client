package org.kaloz.pi4j.client.web

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.TextMessage
import akka.testkit._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages.{PinStatesRequest, PinStatesResponse}
import org.kaloz.pi4j.client.web.WebSocketActor.Initialize
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.{ChangeDigitalInputPinValue, DigitalInputPinValueChangedEvent, DigitalOutputPinValueChangedEvent}
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue.Low
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._

class WebSocketActorSpec
  extends TestKit(ActorSystem("web-test-system"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  override def afterAll() = Await.ready(system.terminate(), 5 seconds)

  "WebSocketActor" should {
    "initialize itself, request initial pin status and send it to the sink" in new scope {
      webSocketActor ! Initialize(sink.ref, client.ref)
      client.expectMsg(PinStatesRequest)

      val pinStatesResponse = PinStatesResponse(Map.empty)
      client.send(webSocketActor, pinStatesResponse)

      sink.expectMsg(TextMessage( """{"type":"init","pins":[]}"""))
    }

    "send text message to the sink when receives a DigitalInputPinValueChangedEvent" in new scope {
      webSocketActor ! Initialize(sink.ref, client.ref)
      client.expectMsg(PinStatesRequest)

      webSocketActor ! DigitalInputPinValueChangedEvent(1, Low)
      sink.expectMsg(TextMessage( """{"type":"stateChange","pin":{"id":1,"mode":"Input","value":"Low"}}"""))
    }

    "send text message to the sink when receives a DigitalOutputPinValueChangedEvent" in new scope {
      webSocketActor ! Initialize(sink.ref, client.ref)
      client.expectMsg(PinStatesRequest)

      webSocketActor ! DigitalOutputPinValueChangedEvent(1, Low)
      sink.expectMsg(TextMessage( """{"type":"stateChange","pin":{"id":1,"mode":"Output","value":"Low"}}"""))
    }

    "send DigitalInputPinValueChangedEvent to the client when a json" in new scope {
      webSocketActor ! Initialize(sink.ref, client.ref)
      client.expectMsg(PinStatesRequest)

      val json = compact(render(parse("""{"type":"pinChangeRequest","pin":{"id":1,"value":"Low"}}""")))
      webSocketActor ! TextMessage.Strict(json)

      client.expectMsg(ChangeDigitalInputPinValue(1,Low))
    }
  }

  private trait scope {
    val sink = TestProbe()
    val client = TestProbe()
    val webSocketActor = system.actorOf(WebSocketActor.props)
  }

}
