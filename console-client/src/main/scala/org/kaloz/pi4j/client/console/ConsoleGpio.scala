package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.Gpio
import org.kaloz.pi4j.client.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.client.messages.ClientMessages.PudMode._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpio(stubClientActor: ActorRef) extends Gpio {

  implicit val timeout = Timeout(1 minutes)

  override def wiringPiSetup: Int = Await.result((stubClientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 1 minute).status

  override def pinMode(pin: Int, mode: Int): Unit = stubClientActor ! PinModeCommand(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = stubClientActor ! PullUpDnControlCommand(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = stubClientActor ! DigitalWriteCommand(pin, value)

  override def pwmWrite(pin: Int, value: Int): Unit = stubClientActor ! PwmWriteCommand(pin, value)

  override def digitalRead(pin: Int): Int = Await.result((stubClientActor ? DigitalReadRequest(pin)).mapTo[DigitalReadResponse], 1 minute).value
}
