package org.kaloz.pi4j.client.web

import com.typesafe.config.ConfigFactory

trait Configuration {

  private lazy val config = ConfigFactory.load("pi4j-client-web")

  lazy val webClientPort = config.getInt("webClient.port")
}
