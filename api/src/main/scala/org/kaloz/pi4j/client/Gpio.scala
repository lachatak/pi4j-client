package org.kaloz.pi4j.client

import com.pi4j.wiringpi.GpioInterruptCallback

trait Gpio {

  def wiringPiSetup: Int = ???

  def wiringPiSetupSys: Int = ???

  def wiringPiSetupGpio: Int = ???

  def wiringPiSetupPhys: Int = ???

  def pinMode(pin: Int, mode: Int): Unit = ???

  def pullUpDnControl(pin: Int, pud: Int): Unit = ???

  def digitalWrite(pin: Int, value: Int): Unit = ???

  def pwmWrite(pin: Int, value: Int): Unit = ???

  def digitalRead(pin: Int): Int = ???

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
