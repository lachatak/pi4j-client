package org.kaloz.pi4j.client

import com.pi4j.io.gpio.{GpioController, GpioFactory, GpioPinDigitalInput, PinPullResistance, RaspiPin}
import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}

object ListenGpioExample extends App {

  println("<--Pi4J--> GPIO Listen Example ... started.")

  val gpio: GpioController = GpioFactory.getInstance
  val myButton: GpioPinDigitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN)

  myButton.addListener(new GpioPinListenerDigital() {
    def handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent) {
      println(" --> GPIO PIN STATE CHANGE: " + event.getPin + " = " + event.getState)
    }
  })

  println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.")

  while (true) {
    Thread.sleep(500)
  }
}

