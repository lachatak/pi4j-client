package org.kaloz.pi4j.client.remote

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.Gpio
import org.kaloz.pi4j.common.messages.ClientMessages.GpioMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class RemoteClientGpio(remoteClientActor: ActorRef) extends Gpio {

  implicit val timeout = Timeout(1 minutes)

  override def wiringPiSetup: Int = Await.result((remoteClientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 1 minute).status

  override def pinMode(pin: Int, mode: Int): Unit = remoteClientActor ! PinModeCommand(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = remoteClientActor ! PullUpDnControlCommand(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = remoteClientActor ! DigitalWriteCommand(pin, value)

  override def pwmWrite(pin: Int, value: Int): Unit = remoteClientActor ! PwmWriteCommand(pin, value)

  override def digitalRead(pin: Int): Int = Await.result((remoteClientActor ? DigitalReadRequest(pin)).mapTo[DigitalReadResponse], 1 minute).value
}
