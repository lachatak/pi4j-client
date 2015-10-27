package org.kaloz.pi4j.client.mock

import akka.actor.ActorRef
import org.kaloz.pi4j.client.GpioUtil

class MockGpioUtil(mockClientActor: ActorRef) extends GpioUtil {
}
