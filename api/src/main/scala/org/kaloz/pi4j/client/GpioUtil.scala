package org.kaloz.pi4j.client

trait GpioUtil {

  def export(pin: Int, direction: Int): Unit = ???

  def unexport(pin: Int): Unit = ???

  def isExported(pin: Int): Boolean = ???

  def setEdgeDetection(pin: Int, edge: Int): Boolean = ???

  def getEdgeDetection(pin: Int): Int = ???

  def setDirection(pin: Int, direction: Int): Boolean = ???

  def getDirection(pin: Int): Int = ???

  def isPinSupported(pin: Int): Int = ???
}
