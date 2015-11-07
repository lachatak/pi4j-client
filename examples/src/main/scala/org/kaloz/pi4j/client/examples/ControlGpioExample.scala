package org.kaloz.pi4j.client.examples

import com.pi4j.io.gpio.{GpioFactory, PinState, RaspiPin}

import scala.util.Try


class ControlGpioExample {

  def runTest(sleep: Long = 5000): Try[Unit] = Try {

    val gpio = GpioFactory.getInstance()

    val pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "MyLED", PinState.HIGH)

    pin.setShutdownOptions(true, PinState.LOW)

    println("--> GPIO state should be: ON")

    Thread.sleep(sleep)

    // turn off gpio pin #01
    pin.low()
    println("--> GPIO state should be: OFF")

    Thread.sleep(sleep)

    // toggle the current state of gpio pin #01 (should turn on)
    pin.toggle()
    println("--> GPIO state should be: ON")

    Thread.sleep(sleep)

    // toggle the current state of gpio pin #01  (should turn off)
    pin.toggle()
    println("--> GPIO state should be: OFF")

    Thread.sleep(sleep)

    // turn on gpio pin #01 for 1 second and then off
    println("--> GPIO state should be: ON for only 1 second")
    pin.pulse(1000, true) // set second argument to 'true' use a blocking call

    // stop all GPIO activity/threads by shutting down the GPIO controller
    // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
    gpio.shutdown()


  }
}

object ControlGpioExample extends App {

  new ControlGpioExample().runTest()
}
