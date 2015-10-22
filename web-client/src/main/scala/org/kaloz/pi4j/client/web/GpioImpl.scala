package org.kaloz.pi4j.client.web

import com.pi4j.wiringpi.{Gpio => JGpio, GpioInterruptCallback}
import org.kaloz.pi4j.client.Gpio

class GpioImpl extends Gpio {

  def wiringPiSetup: Int = JGpio.wiringPiSetup()

  def wiringPiSetupSys: Int = JGpio.wiringPiSetupSys()

  def wiringPiSetupGpio: Int = JGpio.wiringPiSetupGpio()

  def wiringPiSetupPhys: Int = JGpio.wiringPiSetupPhys()

  def pinMode(pin: Int, mode: Int): Unit = JGpio.pinMode(pin, mode)

  def pullUpDnControl(pin: Int, pud: Int): Unit = JGpio.pullUpDnControl(pin, pud)

  def digitalWrite(pin: Int, value: Int): Unit = JGpio.digitalWrite(pin, value)

  def pwmWrite(pin: Int, value: Int): Unit = JGpio.pwmWrite(pin, value)

  def digitalRead(pin: Int): Int = JGpio.digitalRead(pin)

  def analogRead(pin: Int): Int = JGpio.analogRead(pin)

  def analogWrite(pin: Int, value: Int): Unit = JGpio.analogWrite(pin, value)

  def delay(howLong: Long): Unit = JGpio.delay(howLong)

  def millis: Long = JGpio.millis

  def micros: Long = JGpio.micros

  def delayMicroseconds(howLong: Long): Unit = JGpio.delayMicroseconds(howLong)

  def piHiPri(priority: Int): Int = JGpio.piHiPri(priority)

  def waitForInterrupt(pin: Int, timeout: Int): Int = JGpio.waitForInterrupt(pin, timeout)

  def wiringPiISR(pin: Int, edgeType: Int, callback: GpioInterruptCallback): Int = JGpio.wiringPiISR(pin, edgeType, callback)

  def piBoardRev: Int = JGpio.piBoardRev

  def wpiPinToGpio(wpiPin: Int): Int = JGpio.wpiPinToGpio(wpiPin)

  def physPinToGpio(physPin: Int): Int = JGpio.physPinToGpio(physPin)

  def digitalWriteByte(value: Int): Unit = JGpio.digitalWriteByte(value)

  def pwmSetMode(mode: Int): Unit = JGpio.pwmSetMode(mode)

  def pwmSetRange(range: Int): Unit = JGpio.pwmSetRange(range)

  def pwmSetClock(divisor: Int): Unit = JGpio.pwmSetClock(divisor)

  def setPadDrive(group: Int, value: Int): Unit = JGpio.setPadDrive(group, value)

  def getAlt(pin: Int): Int = JGpio.getAlt(pin)

  def gpioClockSet(pin: Int, frequency: Int): Unit = JGpio.gpioClockSet(pin, frequency)
}
