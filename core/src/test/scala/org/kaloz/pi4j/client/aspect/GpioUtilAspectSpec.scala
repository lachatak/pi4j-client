package org.kaloz.pi4j.client.aspect

import org.kaloz.pi4j.client
import org.kaloz.pi4j.client.factory.AbstractClientFactory
import org.mockito.Mockito._
import org.scalatest.{FunSpec, Matchers}

class GpioUtilAspectSpec extends FunSpec with Matchers {

  describe("GpioUtilAspect") {
    it("should weave GpioUtil calls and delegate call to the client instead") {

      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, this.getClass.getPackage.getName)

      GpioUtilWrapper.export(1, 2)
      verify(MockClientFactory.instance.gpioUtil).export(1, 2)

      GpioUtilWrapper.unexport(1)
      verify(MockClientFactory.instance.gpioUtil).unexport(1)

      when(MockClientFactory.instance.gpioUtil.isExported(1)).thenReturn(true)
      GpioUtilWrapper.isExported(1) should be(true)

      when(MockClientFactory.instance.gpioUtil.setEdgeDetection(1, 2)).thenReturn(true)
      GpioUtilWrapper.setEdgeDetection(1, 2) should be(true)

      when(MockClientFactory.instance.gpioUtil.getDirection(1)).thenReturn(2)
      GpioUtilWrapper.getDirection(1) should be(2)

      when(MockClientFactory.instance.gpioUtil.isPinSupported(1)).thenReturn(2)
      GpioUtilWrapper.isPinSupported(1) should be(2)
    }
  }
}

object GpioUtilWrapper extends client.GpioUtil {

  import com.pi4j.wiringpi.{GpioUtil => JGpioUtil}

  override def export(pin: Int, direction: Int): Unit = JGpioUtil.export(pin, direction)

  override def unexport(pin: Int): Unit = JGpioUtil.unexport(pin)

  override def isExported(pin: Int): Boolean = JGpioUtil.isExported(pin)

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = JGpioUtil.setEdgeDetection(pin, edge)

  override def getDirection(pin: Int): Int = JGpioUtil.getDirection(pin)

  override def isPinSupported(pin: Int): Int = JGpioUtil.isPinSupported(pin)
}