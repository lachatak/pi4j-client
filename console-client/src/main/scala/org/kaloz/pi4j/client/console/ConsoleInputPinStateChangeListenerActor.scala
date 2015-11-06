package org.kaloz.pi4j.client.console

import java.util.logging.{Level, Logger}

import akka.actor._
import akka.event.LoggingReceive
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.ChangeInputPinState
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue

class ConsoleInputPinStateChangeListenerActor(pin: Int, key: Char) extends Actor with ActorLogging with NativeKeyListener {

  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    log.info(s"Key listener is activated for pin $pin with '$key'")
    GlobalScreen.addNativeKeyListener(this)
    context.become(handleKeyEvents())
  }

  override def postStop(): Unit = {
    log.info(s"Terminate key listener for $pin and $key")
    GlobalScreen.removeNativeKeyListener(this)
  }

  def handleKeyEvents(keyPressed: Boolean = false): Receive = LoggingReceive {
    case KeyPressed =>
      if (!keyPressed) {
        context.parent ! ChangeInputPinState(pin, PinDigitalValue.High)
        context.become(handleKeyEvents(true))
        log.debug(s"Key Pressed: $key")
      }
    case KeyReleased =>
      if (keyPressed) {
        context.parent ! ChangeInputPinState(pin, PinDigitalValue.Low)
        context.become(handleKeyEvents(false))
        log.debug(s"Key Released: $key")
      }
  }

  def nativeKeyPressed(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyPressed

  def nativeKeyReleased(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyReleased

  def nativeKeyTyped(e: NativeKeyEvent) {}

  case object KeyPressed

  case object KeyReleased

}

object ConsoleInputPinStateChangeListenerActor extends Configuration {

  // Get the logger for "org.jnativehook" and set the level to off.
  val log = Logger.getLogger(classOf[GlobalScreen].getPackage().getName())
  log.setLevel(Level.OFF)
  // Don't forget to disable the parent handlers.
  log.setUseParentHandlers(false)
  GlobalScreen.registerNativeHook()

  def factory: (ActorRefFactory, Int) => ActorRef = (actorRefFactory, pin) => {
    val char = keyMap(pin)
    actorRefFactory.actorOf(Props(classOf[ConsoleInputPinStateChangeListenerActor], pin, char), s"console-$pin-$char-actor")
  }

  def shutdown() = GlobalScreen.unregisterNativeHook()

}
