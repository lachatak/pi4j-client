package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers}

class ListenGpioExampleSpec extends FunSpec with Matchers {

  describe("ListenGpioExample") {
    it("should be finished without exception") {
      new ListenGpioExample().runTest(1000) should be('success)
    }
  }

}
