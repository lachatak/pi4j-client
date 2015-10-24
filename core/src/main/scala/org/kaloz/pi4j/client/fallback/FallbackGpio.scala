package org.kaloz.pi4j.client.fallback

import com.pi4j.wiringpi.{Gpio => JGpio}
import org.kaloz.pi4j.client.Gpio

class FallbackGpio extends Gpio {

  override def wiringPiSetup: Int = JGpio.wiringPiSetup

  override def pinMode(pin: Int, mode: Int): Unit = JGpio.pinMode(pin, mode)

  override def pullUpDnControl(pin: Int, pud: Int): Unit = JGpio.pullUpDnControl(pin, pud)

  override def digitalWrite(pin: Int, value: Int): Unit = JGpio.digitalWrite(pin, value)

  override def pwmWrite(pin: Int, value: Int): Unit = JGpio.pwmWrite(pin, value)

  override def digitalRead(pin: Int): Int = JGpio.digitalRead(pin)
}
