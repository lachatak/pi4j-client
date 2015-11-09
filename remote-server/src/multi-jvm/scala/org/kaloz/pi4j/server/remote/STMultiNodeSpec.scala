package org.kaloz.pi4j.server.remote

import akka.remote.testkit.MultiNodeSpecCallbacks
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

trait STMultiNodeSpec extends MultiNodeSpecCallbacks
with WordSpecLike
with Matchers
with BeforeAndAfterAll {

  override def beforeAll() = multiNodeSpecBeforeAll()

  override def afterAll() = multiNodeSpecAfterAll()
}
