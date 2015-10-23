package org.kaloz.pi4j.client

import akka.util.Timeout
import scala.concurrent.duration._

package object console {

  implicit val timeout = Timeout(1 minutes)
}
