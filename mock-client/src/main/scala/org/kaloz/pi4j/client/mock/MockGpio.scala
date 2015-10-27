package org.kaloz.pi4j.client.mock

import akka.actor.ActorRef
import org.kaloz.pi4j.client.Gpio

class MockGpio(mockClientActor: ActorRef) extends Gpio {
}
