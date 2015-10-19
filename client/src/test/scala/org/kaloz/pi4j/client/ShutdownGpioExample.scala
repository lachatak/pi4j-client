package org.kaloz.pi4j.client

import com.pi4j.io.gpio.{GpioController, GpioFactory, GpioPinDigitalOutput, PinPullResistance, PinState, RaspiPin}

object ShutdownGpioExample extends App {

  println("<--Pi4J--> GPIO Shutdown Example ... started.")

  val gpio: GpioController = GpioFactory.getInstance
  val pin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.HIGH)
  pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF)

  println("--> GPIO state should be: ON")
  println("    This program will automatically terminate in 10 seconds,")
  println("    or you can use the CTRL-C keystroke to terminate at any time.")
  println("    When the program terminates, the GPIO state should be shutdown and set to: OFF")

  Thread.sleep(10000)
  println(" .. shutting down now ...")

  gpio.shutdown
}

