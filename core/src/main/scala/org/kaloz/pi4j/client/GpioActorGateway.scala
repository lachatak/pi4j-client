package org.kaloz.pi4j.client

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.common.messages.ClientMessages.PudMode._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioActorGateway(clientActor: ActorRef) extends Gpio {

  implicit val timeout = Timeout(5 seconds)

  override def wiringPiSetup: Int = Await.result((clientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 5 seconds).status

  override def pinMode(pin: Int, mode: Int): Unit = clientActor ! PinModeCommand(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = clientActor ! PullUpDnControlCommand(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = clientActor ! DigitalWriteCommand(pin, value)

  override def digitalRead(pin: Int): Int = Await.result((clientActor ? DigitalReadRequest(pin)).mapTo[DigitalReadResponse], 5 seconds).value

  override def pwmWrite(pin: Int, value: Int): Unit = clientActor ! PwmWriteCommand(pin, PinPwmValue(value))

}
