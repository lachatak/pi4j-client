package org.kaloz.pi4j.client.web

import scala.io.StdIn

//WE wont need this
object WebApp extends App {
  val webClientFactory = WebClientFactory.instance
  println("Press RETURN to stop...")
  StdIn.readLine()

  webClientFactory.shutdown
  println("Server is down...")
}
