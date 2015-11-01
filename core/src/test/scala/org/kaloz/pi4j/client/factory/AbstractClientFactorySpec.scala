package org.kaloz.pi4j.client.factory

import org.kaloz.pi4j.client.aspect.MockClientFactory
import org.kaloz.pi4j.client.fallback.FallbackClientFactory
import org.scalatest.{OneInstancePerTest, FunSpec, Matchers}

class AbstractClientFactorySpec extends FunSpec with Matchers with OneInstancePerTest {

  describe("AbstractClientFactory") {
    it("should throw ClientFactoryException if there isn't any factory to load") {
      intercept[ClientFactoryException]{
        System.setProperty(AbstractClientFactory.pi4jClientScanPackage, "fake.package")
        new AbstractClientFactory().factory
      }
    }

    it("should pick the only client factory if there is only client available") {
      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, classOf[MockClientFactory].getPackage.getName)
      new AbstractClientFactory().factory shouldBe a[MockClientFactory]
    }

    it("should return with the selected client mode factory if the pi4j.client.mode property is provided at runtime and the type is availbe") {
      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, this.getClass.getPackage.getName)
      System.setProperty(AbstractClientFactory.pi4jClientMode, "console")
      new AbstractClientFactory().factory shouldBe a[ConsoleClientFactory]
    }

    it("should return the fallback client mode if it the pi4j.client.mode property is provided at runtime but the type doesn't exist") {
      System.setProperty(AbstractClientFactory.pi4jClientScanPackage, this.getClass.getPackage.getName)
      System.setProperty(AbstractClientFactory.pi4jClientMode, "fake.type")
      new AbstractClientFactory().factory shouldBe a[FallbackClientFactory]
    }
  }
}
