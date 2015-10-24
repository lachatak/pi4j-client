package org.kaloz.pi4j.client.console

import java.util.logging.Level

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}
import org.kaloz.pi4j.client.messages.ClientMessages.PinStateChange.PinStateChange
import org.kaloz.pi4j.client.messages.ClientMessages.PinValue

class ConsoleListenerActor(pin: Int, key: Char) extends Actor with ActorLogging with NativeKeyListener {

  log.info(s"Key listener is activated for pin $pin with '$key'")
  GlobalScreen.addNativeKeyListener(this)

  override def receive: Receive = Actor.emptyBehavior

  context.become(handle())

  def handle(keyPressed: Boolean = false): Receive = {
    case KeyPressed =>
      if (keyPressed == false) {
        context.system.eventStream.publish(PinStateChange(pin, PinValue.High))
        context.become(handle(true))
        log.debug(s"Key Pressed: $key")
      }
    case KeyReleased =>
      if (keyPressed == true) {
        context.system.eventStream.publish(PinStateChange(pin, PinValue.Low))
        context.become(handle(false))
        log.debug(s"Key Released: $key")
      }
    case Terminated =>
      log.info(s"Terminate key listener for $key")
      GlobalScreen.removeNativeKeyListener(this)
  }

  def nativeKeyPressed(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyPressed

  def nativeKeyReleased(e: NativeKeyEvent): Unit = if (key == getKeyText(e.getKeyCode()).charAt(0).toUpper) self ! KeyReleased

  def nativeKeyTyped(e: NativeKeyEvent) {}

  case object KeyPressed

  case object KeyReleased

}

object ConsoleListenerActor {

  GlobalScreen.registerNativeHook()
  java.util.logging.Logger.getLogger("org.jnativehook").setLevel(Level.OFF)

  def props: (Int, Char) => Props = (pin, char) => Props(classOf[ConsoleListenerActor], pin, char)

}
