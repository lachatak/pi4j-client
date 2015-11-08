package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers}

class TriggerGpioExampleSpec extends FunSpec with Matchers {

  describe("TriggerGpioExample") {
    it("should be finished without exception") {
      new TriggerGpioExample().runTest(1000) should be('success)
    }
  }

}
