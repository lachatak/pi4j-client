package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers}

class ShutdownGpioExampleSpec extends FunSpec with Matchers {

  describe("ShutdownGpioExample") {
    it("should be finished without exception") {
      new ShutdownGpioExample().runTest() should be('success)
    }
  }

}
