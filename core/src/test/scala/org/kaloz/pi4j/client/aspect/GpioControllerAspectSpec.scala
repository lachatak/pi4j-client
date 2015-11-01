package org.kaloz.pi4j.client.aspect

import org.kaloz.pi4j.client.factory.AbstractClientFactory
import org.mockito.Mockito._
import org.scalatest.{FunSpec, Matchers}

class GpioControllerAspectSpec extends FunSpec with Matchers {

  describe("GpioControllerAspect") {
    it("should weave GpioController.shutdown call and delegate call to the client instead") {

      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, this.getClass.getPackage.getName)

      GpioControllerWrapper.shutdown()
      GpioControllerWrapper.shutdown()
      verify(MockClientFactory.instance.gpioController, times(1)).shutdown()
    }
  }
}

object GpioControllerWrapper {

  import com.pi4j.io.gpio.impl.{GpioControllerImpl => JGpioControllerImpl}

  val controller = new JGpioControllerImpl()

  def shutdown(): Unit = controller.shutdown()

}