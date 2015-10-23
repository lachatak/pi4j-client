package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import org.kaloz.pi4j.client.Gpio
import org.kaloz.pi4j.client.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.client.messages.ClientMessages.PudMode._
import org.kaloz.pi4j.client.messages.ClientMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpio(stubClientActor: ActorRef) extends Gpio {

  override def wiringPiSetup: Int = Await.result((stubClientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 1 minute).status

  override def pinMode(pin: Int, mode: Int): Unit = Await.result((stubClientActor ? PinModeCommand(pin, mode)).mapTo[Done.type], 1 minute)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = Await.result((stubClientActor ? PullUpDnControlCommand(pin, pud)).mapTo[Done.type], 1 minute)

  override def digitalWrite(pin: Int, value: Int): Unit = Await.result((stubClientActor ? DigitalWriteCommand(pin, value)).mapTo[Done.type], 1 minute)

  override def pwmWrite(pin: Int, value: Int): Unit = Await.result((stubClientActor ? PwmWriteCommand(pin, value)).mapTo[Done.type], 1 minute)

  override def digitalRead(pin: Int): Int = Await.result((stubClientActor ? DigitalReadRequest(pin)).mapTo[DigitalReadResponse], 1 minute).value
}
