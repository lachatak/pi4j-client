package org.kaloz.pi4j.client.pi

import com.pi4j.wiringpi.{Gpio => JGpio, GpioInterruptCallback}
import org.kaloz.pi4j.client.Gpio

class PiGpio extends Gpio {

  override def wiringPiSetup: Int = JGpio.wiringPiSetup()

  override def wiringPiSetupSys: Int = JGpio.wiringPiSetupSys()

  override def wiringPiSetupGpio: Int = JGpio.wiringPiSetupGpio()

  override def wiringPiSetupPhys: Int = JGpio.wiringPiSetupPhys()

  override def pinMode(pin: Int, mode: Int): Unit = JGpio.pinMode(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = JGpio.pullUpDnControl(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = JGpio.digitalWrite(pin, value)

  override def pwmWrite(pin: Int, value: Int): Unit = JGpio.pwmWrite(pin, value)

  override def digitalRead(pin: Int): Int = JGpio.digitalRead(pin)

  override def analogRead(pin: Int): Int = JGpio.analogRead(pin)

  override def analogWrite(pin: Int, value: Int): Unit = JGpio.analogWrite(pin, value)

  override def delay(howLong: Long): Unit = JGpio.delay(howLong)

  override def millis: Long = JGpio.millis

  override def micros: Long = JGpio.micros

  override def delayMicroseconds(howLong: Long): Unit = JGpio.delayMicroseconds(howLong)

  override def piHiPri(priority: Int): Int = JGpio.piHiPri(priority)

  override def waitForInterrupt(pin: Int, timeout: Int): Int = JGpio.waitForInterrupt(pin, timeout)

  override def wiringPiISR(pin: Int, edgeType: Int, callback: GpioInterruptCallback): Int = JGpio.wiringPiISR(pin, edgeType, callback)

  override def piBoardRev: Int = JGpio.piBoardRev

  override def wpiPinToGpio(wpiPin: Int): Int = JGpio.wpiPinToGpio(wpiPin)

  override def physPinToGpio(physPin: Int): Int = JGpio.physPinToGpio(physPin)

  override def digitalWriteByte(value: Int): Unit = JGpio.digitalWriteByte(value)

  override def pwmSetMode(mode: Int): Unit = JGpio.pwmSetMode(mode)

  override def pwmSetRange(range: Int): Unit = JGpio.pwmSetRange(range)

  override def pwmSetClock(divisor: Int): Unit = JGpio.pwmSetClock(divisor)

  override def setPadDrive(group: Int, value: Int): Unit = JGpio.setPadDrive(group, value)

  override def getAlt(pin: Int): Int = JGpio.getAlt(pin)

  override def gpioClockSet(pin: Int, frequency: Int): Unit = JGpio.gpioClockSet(pin, frequency)
}
