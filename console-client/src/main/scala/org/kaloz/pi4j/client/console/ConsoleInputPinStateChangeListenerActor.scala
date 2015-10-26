package org.kaloz.pi4j.client.console

import java.util.logging.Level

import akka.actor._
import akka.event.LoggingReceive
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}
import org.kaloz.pi4j.common.messages.ClientMessages.PinStateChange.ChangeInputPinState
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue

class ConsoleInputPinStateChangeListenerActor(pin: Int, key: Char) extends Actor with ActorLogging with NativeKeyListener {

  log.info(s"Key listener is activated for pin $pin with '$key'")
  GlobalScreen.addNativeKeyListener(this)

  override def receive: Receive = Actor.emptyBehavior

  context.become(handleKeyEvents())

  def handleKeyEvents(keyPressed: Boolean = false): Receive = LoggingReceive {
    case KeyPressed =>
      if (keyPressed == false) {
        context.parent ! ChangeInputPinState(pin, PinValue.High)
        context.become(handleKeyEvents(true))
        log.debug(s"Key Pressed: $key")
      }
    case KeyReleased =>
      if (keyPressed == true) {
        context.parent ! ChangeInputPinState(pin, PinValue.Low)
        context.become(handleKeyEvents(false))
        log.debug(s"Key Released: $key")
      }
  }

  override def postStop(): Unit = {
    log.info(s"Terminate key listener for $pin and $key")
    GlobalScreen.removeNativeKeyListener(this)
  }

  def nativeKeyPressed(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyPressed

  def nativeKeyReleased(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyReleased

  def nativeKeyTyped(e: NativeKeyEvent) {}

  case object KeyPressed

  case object KeyReleased

}

object ConsoleInputPinStateChangeListenerActor {

  def factory: (ActorRefFactory, Int, Char) => ActorRef = (factory, pin, char) => {
    GlobalScreen.registerNativeHook()
    java.util.logging.Logger.getLogger("org.jnativehook").setLevel(Level.OFF)

    factory.actorOf(Props(classOf[ConsoleInputPinStateChangeListenerActor], pin, char), s"console-$pin-$char-actor")
  }

}
