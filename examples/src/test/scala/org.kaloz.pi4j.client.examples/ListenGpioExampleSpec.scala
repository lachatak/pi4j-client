package org.kaloz.pi4j.client.examples

import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}

class ListenGpioExampleSpec extends FunSpec with Matchers with OneInstancePerTest {

  describe("ListenGpioExample") {
    it("should be finished without exception") {
      new ListenGpioExample().runTest(1000) should be('success)
    }
  }

}
