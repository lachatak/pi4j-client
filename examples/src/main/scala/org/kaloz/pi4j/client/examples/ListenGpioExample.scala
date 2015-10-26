package org.kaloz.pi4j.client.examples

import com.pi4j.io.gpio._
import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}

object ListenGpioExample extends App {

  println("<--Pi4J--> GPIO Listen Example ... started.")

  val gpio: GpioController = GpioFactory.getInstance
  val myButton: GpioPinDigitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP)

  myButton.addListener(new GpioPinListenerDigital() {
    def handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent) {
      println(" --> GPIO PIN STATE CHANGE: " + event.getPin + " = " + event.getState)
    }
  })

  println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.")

  Thread.sleep(10000)

  gpio.shutdown()
}
