package org.kaloz.pi4j.client.web

import com.pi4j.wiringpi.{GpioUtil => JGpioUtil}
import org.kaloz.pi4j.client.GpioUtil

class GpioUtilImpl extends GpioUtil {

  @throws(classOf[RuntimeException])
  def export(pin: Int, direction: Int): Unit = JGpioUtil.export(pin, direction)

  @throws(classOf[RuntimeException])
  def unexport(pin: Int): Unit = JGpioUtil.unexport(pin)

  @throws(classOf[RuntimeException])
  def isExported(pin: Int): Boolean = JGpioUtil.isExported(pin)

  @throws(classOf[RuntimeException])
  def setEdgeDetection(pin: Int, edge: Int): Boolean = JGpioUtil.setEdgeDetection(pin, edge)

  @throws(classOf[RuntimeException])
  def getEdgeDetection(pin: Int): Int = JGpioUtil.getEdgeDetection(pin)

  @throws(classOf[RuntimeException])
  def setDirection(pin: Int, direction: Int): Boolean = JGpioUtil.setDirection(pin, direction)

  @throws(classOf[RuntimeException])
  def getDirection(pin: Int): Int = JGpioUtil.getDirection(pin)

  @throws(classOf[RuntimeException])
  def isPinSupported(pin: Int): Int = JGpioUtil.isPinSupported(pin)
}
