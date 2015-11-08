package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers}

class ControlGpioExampleSpec extends FunSpec with Matchers {

  describe("ControlGpioExample") {
    it("should be finished without exception") {
      new ControlGpioExample().runTest(1000) should be('success)
    }
  }

}
