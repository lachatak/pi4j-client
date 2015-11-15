package org.kaloz.pi4j.client.web

import akka.http.scaladsl.model.ws.TextMessage
import org.json4s._
import org.json4s.native.JsonMethods._
import org.kaloz.pi4j.client.actor.InMemoryClientActor.ServiceMessages.Pin
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode.{Input, Output}
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue.{High, Low}
import org.scalatest.{FunSpec, Matchers}

class WebSocketMessageFactorySpec extends FunSpec with Matchers {

  describe("initData") {
    it("should create init data json TextMessage from pins Map") {
      val pins = Map(
        0 -> Pin(mode = Input, value = Low),
        1 -> Pin(mode = Input, value = High),
        2 -> Pin(mode = Output, value = Low),
        3 -> Pin(mode = Output, value = High)
      )
      val json = """{
                   |"type":"init",
                   |"pins":[
                   |    {"id":0,"mode":"Input","value":"Low"},
                   |    {"id":1,"mode":"Input","value":"High"},
                   |    {"id":2,"mode":"Output","value":"Low"},
                   |    {"id":3,"mode":"Output","value":"High"}
                   |]}""".stripMargin

      WebSocketMessageFactory.initData(pins) shouldBe TextMessage(compact(render(parse(json))))
    }
  }

  describe("inputChange") {
    it("should create stateChange json TextMessage from output pin") {
      val json = """{"type":"stateChange", "pin": {"id":1,"mode":"Input","value":"Low"}}"""
      WebSocketMessageFactory.inputChange(1, Low) shouldBe TextMessage(compact(render(parse(json))))
    }
  }

  describe("outputChange") {
    it("should create stateChange json TextMessage from input pin") {
      val json = """{"type":"stateChange", "pin": {"id":1,"mode":"Output","value":"Low"}}"""
      WebSocketMessageFactory.outputChange(1, Low) shouldBe TextMessage(compact(render(parse(json))))
    }
  }
}