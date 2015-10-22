package org.kaloz.pi4j.client

trait GpioUtil {

  @throws(classOf[RuntimeException])
  def export(pin: Int, direction: Int): Unit

  @throws(classOf[RuntimeException])
  def unexport(pin: Int): Unit

  @throws(classOf[RuntimeException])
  def isExported(pin: Int): Boolean

  @throws(classOf[RuntimeException])
  def setEdgeDetection(pin: Int, edge: Int): Boolean

  @throws(classOf[RuntimeException])
  def getEdgeDetection(pin: Int): Int

  @throws(classOf[RuntimeException])
  def setDirection(pin: Int, direction: Int): Boolean

  @throws(classOf[RuntimeException])
  def getDirection(pin: Int): Int

  @throws(classOf[RuntimeException])
  def isPinSupported(pin: Int): Int
}
