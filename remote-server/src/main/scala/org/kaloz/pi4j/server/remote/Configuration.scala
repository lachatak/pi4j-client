package org.kaloz.pi4j.server.remote

import com.typesafe.config.ConfigFactory

trait Configuration {

  lazy val config = ConfigFactory.load("pi4j-remote-server")
}
