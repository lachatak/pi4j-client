package org.kaloz.pi4j.client.fallback

import com.pi4j.wiringpi.{GpioUtil => JGpioUtil}
import org.kaloz.pi4j.client.GpioUtil

class FallbackGpioUtil extends GpioUtil {

  override def export(pin: Int, direction: Int): Unit = JGpioUtil.export(pin, direction)

  override def unexport(pin: Int): Unit = JGpioUtil.unexport(pin)

  override def isExported(pin: Int): Boolean = JGpioUtil.isExported(pin)

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = JGpioUtil.setEdgeDetection(pin, edge)

  override def getDirection(pin: Int): Int = JGpioUtil.getDirection(pin)

  override def isPinSupported(pin: Int): Int = JGpioUtil.isPinSupported(pin)
}
