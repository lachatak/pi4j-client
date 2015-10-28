package org.kaloz.pi4j.client.web

//class WebClientHttpServiceActor(gpio: Gpio) extends HttpServiceActor {
//  private implicit val system = context.system
//
//  def receive = runRoute {
//    path("digitalWrite" / IntNumber) { pin =>
//      put {
//        entity(as[String]) { pinValue =>
//          gpio.digitalWrite(pin, pinValue.toInt)
//          complete(s"write: $pin -> $pinValue\n")
//        }
//      }
//    } ~
//    path("digitalRead" / IntNumber) { pin =>
//      get {
//        complete(s"read: $pin -> ${gpio.digitalRead(pin)}\n")
//      }
//    }
//  }
//}
//
//object WebClientHttpServiceActor {
//  def props(gpio: Gpio) = Props(classOf[WebClientHttpServiceActor], gpio)
//}