package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}

class TriggerGpioExampleSpec extends FunSpec with Matchers with OneInstancePerTest {

  describe("TriggerGpioExample") {
    it("should be finished without exception") {
      new TriggerGpioExample().runTest() should be('success)
    }
  }

}
