package org.kaloz.pi4j.client

import com.pi4j.io.gpio.{GpioFactory, PinState, RaspiPin}

object ControlGpioExample extends App {

  val gpio = GpioFactory.getInstance()

  val pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH)

  pin.setShutdownOptions(true, PinState.LOW)

  println("--> GPIO state should be: ON")

  Thread.sleep(5000)

  // turn off gpio pin #01
  pin.low()
  println("--> GPIO state should be: OFF")

  Thread.sleep(5000)

  // toggle the current state of gpio pin #01 (should turn on)
  pin.toggle()
  println("--> GPIO state should be: ON")

  Thread.sleep(5000)

  // toggle the current state of gpio pin #01  (should turn off)
  pin.toggle()
  println("--> GPIO state should be: OFF")

  Thread.sleep(5000)

  // turn on gpio pin #01 for 1 second and then off
  println("--> GPIO state should be: ON for only 1 second")
  pin.pulse(1000, true) // set second argument to 'true' use a blocking call

  // stop all GPIO activity/threads by shutting down the GPIO controller
  // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
  gpio.shutdown()
}
