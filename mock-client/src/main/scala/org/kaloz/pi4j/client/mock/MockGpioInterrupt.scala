package org.kaloz.pi4j.client.mock

import akka.actor.ActorRef
import org.kaloz.pi4j.client.GpioInterrupt

class MockGpioInterrupt(mockClientActor: ActorRef) extends GpioInterrupt {
}
