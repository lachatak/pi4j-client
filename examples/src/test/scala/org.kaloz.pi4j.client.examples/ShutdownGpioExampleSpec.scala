package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}

class ShutdownGpioExampleSpec extends FunSpec with Matchers with OneInstancePerTest {

  describe("ShutdownGpioExample") {
    it("should be finished without exception") {
      new ShutdownGpioExample().runTest() should be('success)
    }
  }

}
