package org.kaloz.pi4j.client.console

import org.scalatest.{FunSpec, Matchers}

class ConfigurationSpec extends FunSpec with Matchers {

  describe("Configuration") {
    it("should give back the upper case char belongs to the input pin number from the configuration") {
      val configuration = new Configuration {}
      configuration.keyMap(1) should be('Q')
    }
  }
}
