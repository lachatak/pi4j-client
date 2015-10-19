package org.kaloz.pi4j.client

import java.util.concurrent.Callable

import com.pi4j.io.gpio.trigger.{GpioCallbackTrigger, GpioPulseStateTrigger, GpioSetStateTrigger, GpioSyncStateTrigger}
import com.pi4j.io.gpio.{GpioController, GpioFactory, GpioPinDigitalInput, PinPullResistance, PinState, RaspiPin}

object TriggerGpioExample extends App {

  System.out.println("<--Pi4J--> GPIO Trigger Example ... started.")

  val gpio: GpioController = GpioFactory.getInstance
  val myButton: GpioPinDigitalInput = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN)

  System.out.println(" ... complete the GPIO #02 circuit and see the triggers take effect.")

  val myLed =
    List(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "LED #1", PinState.LOW),
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
  while (true) {
    Thread.sleep(500)
  }
}

