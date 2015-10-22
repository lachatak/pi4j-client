package org.kaloz.pi4j.client.console

import java.util.logging.Level

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import com.pi4j.wiringpi.GpioInterrupt
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}

class PinStateChangeActor(pin: Int, key: Char) extends Actor with ActorLogging with NativeKeyListener {

  log.info(s"Key listener is activated for pin $pin with '$key'")
  GlobalScreen.addNativeKeyListener(this)

  override def receive: Receive = Actor.emptyBehavior

  context.become(handle())

  //Mimic native callback
  val parameterTypes = List(classOf[Int], classOf[Boolean])
  val m = classOf[GpioInterrupt].getDeclaredMethod("pinStateChangeCallback", parameterTypes: _*)
  m.setAccessible(true)

  def handle(keyPressed: Boolean = false): Receive = {
    case KeyPressed =>
      if (keyPressed == false) {
        m.invoke(null, pin: java.lang.Integer, true: java.lang.Boolean)
        context.become(handle(true))
        log.info(s"Key Pressed: $key")
      }
    case KeyReleased =>
      if (keyPressed == true) {
        m.invoke(null, pin: java.lang.Integer, false: java.lang.Boolean)
        context.become(handle(false))
        log.info(s"Key Released: $key")
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

object PinStateChangeActor {

  GlobalScreen.registerNativeHook()
  java.util.logging.Logger.getLogger("org.jnativehook").setLevel(Level.OFF)

  def props(pin: Int, char: Char) = Props(classOf[PinStateChangeActor], pin, char)

}
