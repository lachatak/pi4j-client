package org.kaloz.pi4j.client.console

import java.util.logging.{Level, Logger}

import akka.actor._
import akka.event.LoggingReceive
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}
import org.kaloz.pi4j.client.actor.InMemoryClientActor.CreatePinStateChangeCallback
import org.kaloz.pi4j.common.messages.ClientMessages.DigitalPinValueChange.ChangeDigitalInputPinValue
import org.kaloz.pi4j.common.messages.ClientMessages.PinValue.PinDigitalValue

class ConsoleInputPinStateChangeListenerActor(pin: Int, key: Char, parent: ActorRef) extends Actor with ActorLogging with NativeKeyListener {

  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    log.info(s"Key listener is activated for pin $pin with '$key' with parent $parent")
    GlobalScreen.addNativeKeyListener(this)
    context.become(handleKeyEvents())
  }

  override def postStop(): Unit = {
    log.info(s"Terminate key listener for pin $pin with '$key' with parent $parent")
    GlobalScreen.removeNativeKeyListener(this)
  }

  def handleKeyEvents(keyPressed: Boolean = false): Receive = LoggingReceive {
    case KeyPressed =>
      if (!keyPressed) {
        parent ! ChangeDigitalInputPinValue(pin, PinDigitalValue.High)
        context.become(handleKeyEvents(true))
        log.debug(s"Key Pressed: $key")
      }
    case KeyReleased =>
      if (keyPressed) {
        parent ! ChangeDigitalInputPinValue(pin, PinDigitalValue.Low)
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

class ConsoleInputPinStateChangeListenerActorFactory(actorRefFactory: ActorRefFactory) extends Actor with ActorLogging with Configuration {

  // Get the logger for "org.jnativehook" and set the level to off.
  val logGlobalScreen = Logger.getLogger(classOf[GlobalScreen].getPackage().getName())
  logGlobalScreen.setLevel(Level.OFF)
  // Don't forget to disable the parent handlers.
  logGlobalScreen.setUseParentHandlers(false)
  GlobalScreen.registerNativeHook()

  override def receive: Actor.Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    context.become(manageListeners())
  }

  override def postStop(): Unit = {
    GlobalScreen.unregisterNativeHook()
  }

  def manageListeners(listeners: Map[Int, ActorRef] = Map.empty): Receive = LoggingReceive {
    case CreatePinStateChangeCallback(pin, parent) =>
      listeners.get(pin) match {
        case Some(listener) => sender ! listener
        case None =>
          val key = keyMap(pin)
          val listener = actorRefFactory.actorOf(Props(classOf[ConsoleInputPinStateChangeListenerActor], pin, key, parent), s"console-$pin-$key-actor")
          context.watch(listener)
          context.become(manageListeners(listeners + (pin -> listener)))
          log.debug(s"Listener actor created for pin $pin with '$key'")
          sender ! listener
      }
    case Terminated(terminatedListener) =>
      context.become(manageListeners(listeners.filterNot(entry => entry._2 == terminatedListener)))
      log.debug(s"Listener actor deleted from the registry $terminatedListener")
  }
}

object ConsoleInputPinStateChangeListenerActorFactory {

  def props()(implicit actorSystem: ActorRefFactory) = Props(classOf[ConsoleInputPinStateChangeListenerActorFactory], actorSystem)

}

