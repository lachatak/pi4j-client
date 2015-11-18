package org.kaloz.pi4j.client.web

import akka.actor._
import akka.http.scaladsl.model.ws._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging
import org.json4s._
import org.json4s.native.JsonMethods._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages._
import org.kaloz.pi4j.client.web.WebSocketActor.Initialize
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange._

import scala.concurrent.duration._

class WebSocketActor extends Actor with StrictLogging {

  implicit val jsonFormat = org.json4s.DefaultFormats
  context.system.eventStream.subscribe(self, classOf[DigitalInputPinValueChangedEvent])
  context.system.eventStream.subscribe(self, classOf[DigitalOutputPinValueChangedEvent])

  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case Initialize(sink, webClient) => {
      webClient ! PinStatesRequest
      context.become(receiveWithSink(sink, webClient))
    }
  }

  def receiveWithSink(sink: ActorRef, webClient: ActorRef): Receive = {
    case PinStatesResponse(pins) => sink ! WebSocketMessageFactory.initData(pins)
    case DigitalInputPinValueChangedEvent(id, pinValue) => sink ! WebSocketMessageFactory.inputChange(id, pinValue)
    case DigitalOutputPinValueChangedEvent(id, pinValue) => sink ! WebSocketMessageFactory.outputChange(id, pinValue)
    case TextMessage.Strict(json) => handleJson(json, webClient)
  }

  private def handleJson(jsonString: String, webClient: ActorRef): Unit = {
    val json = parse(jsonString)
    (json \ "type").extractOpt[String] map (_ match {
      case "pinChangeRequest" => webClient ! ChangeDigitalInputPinValue((json \ "pin" \ "id").extract[Int], (json \ "pin" \ "value").extract[String])
    })
  }
}

object WebSocketActor {

  def webSocketActorFlow(webSocketActor: ActorRef, webClientActor: ActorRef): Flow[Message, Message, _] = {
    val in = Flow[Message].to(Sink.actorRef(webSocketActor, PoisonPill))

    val out = Source.actorRef(10, OverflowStrategy.fail).mapMaterializedValue(sink => webSocketActor ! Initialize(sink, webClientActor))
    Flow.wrap(in, out)(Keep.both)
  }

  case class Initialize(sink: ActorRef, webClient: ActorRef)
}