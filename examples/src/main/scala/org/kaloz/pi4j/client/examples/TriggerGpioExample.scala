package org.kaloz.pi4j.client.examples

import java.util.concurrent.Callable

import com.pi4j.io.gpio._
import com.pi4j.io.gpio.trigger.{GpioCallbackTrigger, GpioPulseStateTrigger, GpioSetStateTrigger, GpioSyncStateTrigger}

import scala.util.Try

class TriggerGpioExample {

  def runTest(sleep: Long = 10000): Try[Unit] = Try {

    System.out.println("<--Pi4J--> GPIO Trigger Example ... started.")

    val gpio: GpioController = GpioFactory.getInstance
    val myButton: GpioPinDigitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP)

    System.out.println(" ... complete the GPIO #02 circuit and see the triggers take effect.")

    val myLed =
      List(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "LED #1", PinState.LOW),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "LED #2", PinState.LOW),
        gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "LED #3", PinState.LOW))

    myButton.addTrigger(new GpioSetStateTrigger(PinState.HIGH, myLed(0), PinState.HIGH))
    myButton.addTrigger(new GpioSetStateTrigger(PinState.LOW, myLed(0), PinState.LOW))
    myButton.addTrigger(new GpioSyncStateTrigger(myLed(1)))
    myButton.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, myLed(2), 1000))

    myButton.addTrigger(new GpioCallbackTrigger(new Callable[Void] {
      override def call(): Void = {
        System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ")
        null
      }
    }))

    Thread.sleep(sleep)

    gpio.shutdown()
  }
}

object TriggerGpioExample extends App {

  new TriggerGpioExample().runTest(2000)
}
