package org.kaloz.pi4j.client.examples

import com.pi4j.io.gpio._

object ShutdownGpioExample extends App {

  println("<--Pi4J--> GPIO Shutdown Example ... started.")

  val gpio: GpioController = GpioFactory.getInstance
  val pin: GpioPinDigitalOutput = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.HIGH)
  pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF)

  println("--> GPIO state should be: ON")
  println("    This program will automatically terminate in 2 seconds,")
  println("    or you can use the CTRL-C keystroke to terminate at any time.")
  println("    When the program terminates, the GPIO state should be shutdown and set to: OFF")

  Thread.sleep(2000)
  println(" .. shutting down now ...")

  gpio.shutdown
}
