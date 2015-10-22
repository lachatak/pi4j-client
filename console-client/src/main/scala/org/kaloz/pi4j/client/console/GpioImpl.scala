package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.pi4j.wiringpi.GpioInterruptCallback
import org.kaloz.pi4j.client.Gpio
import org.kaloz.pi4j.client.messages.ClientMessages.GpioMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinMode._
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue._
import org.kaloz.pi4j.client.messages.ClientMessages.PudMode._
import org.kaloz.pi4j.client.messages.ClientMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioImpl(stubClientActor: ActorRef) extends Gpio {

  implicit val timeout = Timeout(1 minutes)

  def wiringPiSetup: Int = Await.result((stubClientActor ? WiringPiSetupRequest).mapTo[WiringPiSetupResponse], 1 minute).status

  def wiringPiSetupSys: Int = ???

  def wiringPiSetupGpio: Int = ???

  def wiringPiSetupPhys: Int = ???

  def pinMode(pin: Int, mode: Int): Unit = Await.result((stubClientActor ? PinModeCommand(pin, mode)).mapTo[Done.type], 1 minute)

  def pullUpDnControl(pin: Int, pud: Int): Unit = Await.result((stubClientActor ? PullUpDnControlCommand(pin, pud)).mapTo[Done.type], 1 minute)

  def digitalWrite(pin: Int, value: Int): Unit = Await.result((stubClientActor ? DigitalWriteCommand(pin, value)).mapTo[Done.type], 1 minute)

  def pwmWrite(pin: Int, value: Int): Unit = Await.result((stubClientActor ? PwmWriteCommand(pin, value)).mapTo[Done.type], 1 minute)

  def digitalRead(pin: Int): Int = Await.result((stubClientActor ? DigitalReadRequest(pin)).mapTo[DigitalReadResponse], 1 minute).value

  def analogRead(pin: Int): Int = ???

  def analogWrite(pin: Int, value: Int): Unit = ???

  def delay(howLong: Long): Unit = ???

  def millis: Long = ???

  def micros: Long = ???

  def delayMicroseconds(howLong: Long): Unit = ???

  def piHiPri(priority: Int): Int = ???

  def waitForInterrupt(pin: Int, timeout: Int): Int = ???

  def wiringPiISR(pin: Int, edgeType: Int, callback: GpioInterruptCallback): Int = ???

  def piBoardRev: Int = ???

  def wpiPinToGpio(wpiPin: Int): Int = ???

  def physPinToGpio(physPin: Int): Int = ???

  def digitalWriteByte(value: Int): Unit = ???

  def pwmSetMode(mode: Int): Unit = ???

  def pwmSetRange(range: Int): Unit = ???

  def pwmSetClock(divisor: Int): Unit = ???

  def setPadDrive(group: Int, value: Int): Unit = ???

  def getAlt(pin: Int): Int = ???

  def gpioClockSet(pin: Int, frequency: Int): Unit = ???
}
