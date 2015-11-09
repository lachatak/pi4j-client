package org.kaloz.pi4j.client.web

import akka.http.scaladsl.model.ws.TextMessage
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages.Pin
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode.{Input, Output, PinMode}
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue.PinDigitalValue

object WebSocketMessageFactory {

  def initData(pins: Map[Int, Pin]) = asTextMessage(initJson(pins))

  def inputChange(id: Int, pinValue: PinDigitalValue) = asTextMessage(stateChange(id, Input, pinValue))

  def outputChange(id: Int, pinValue: PinDigitalValue) = asTextMessage(stateChange(id, Output, pinValue))

  private def asTextMessage(json: JObject) = TextMessage(compact(render(json)))

  private def initJson(pins: Map[Int, Pin]): JObject = {
    val pinsJson = pins.map { case (id, pin: Pin) => pinToJson(id, pin.mode, pin.value) }
    ("type" -> "init") ~ ("pins" -> pinsJson)
  }

  private def stateChange(id: Int, mode: PinMode, pinValue: PinDigitalValue): JObject = {
    ("type" -> "stateChange") ~ ("pin" -> pinToJson(id, mode, pinValue))
  }

  private def pinToJson(id: Int, mode: PinMode, pinValue: PinDigitalValue): JObject = {
    val modeString: String = mode
    val pinValueString: String = pinValue
    ("id" -> id) ~ ("mode" -> modeString) ~ ("value" -> pinValueString)
  }
}