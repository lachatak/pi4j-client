package org.kaloz.pi4j.client

import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent._
import org.jnativehook.keyboard.{NativeKeyEvent, NativeKeyListener}

object GlobalKeyListenerExample extends App with NativeKeyListener {

  try {
    GlobalScreen.registerNativeHook()
  }
  catch {
    case ex: Exception =>
      println("There was a problem registering the native hook.")
      println(ex.getMessage())
      System.exit(1)
  }

  //Construct the example object and initialze native hook.
  GlobalScreen.addNativeKeyListener(this)


  def nativeKeyPressed(e: NativeKeyEvent) {
    println(s"Key Pressed: ${NativeKeyEvent.getKeyText(e.getKeyCode())}")
    if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
      GlobalScreen.unregisterNativeHook()
    }
  }

  def nativeKeyReleased(e: NativeKeyEvent) {
    println(s"Key Released: ${NativeKeyEvent.getKeyText(e.getKeyCode())}")
  }

  def nativeKeyTyped(e: NativeKeyEvent) {
    println(s"Key Typed: ${getKeyText(e.getKeyCode())}")
  }

}
