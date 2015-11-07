package org.kaloz.pi4j.client.examples

import com.pi4j.io.gpio._
import com.pi4j.io.gpio.event.{GpioPinDigitalStateChangeEvent, GpioPinListenerDigital}

import scala.util.Try

class ListenGpioExample {

  def runTest(sleep: Long = 10000): Try[Unit] = Try {
    println("<--Pi4J--> GPIO Listen Example ... started.")

    val gpio: GpioController = GpioFactory.getInstance
    val myButton: GpioPinDigitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP)

    myButton.addListener(new GpioPinListenerDigital() {
      def handleGpioPinDigitalStateChangeEvent(event: GpioPinDigitalStateChangeEvent) {
        println(" --> GPIO PIN STATE CHANGE: " + event.getPin + " = " + event.getState)
      }
    })

    println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.")

    Thread.sleep(sleep)

    gpio.shutdown()
  }
}

object ListenGpioExample extends App {

  new ListenGpioExample().runTest(2000)
}
