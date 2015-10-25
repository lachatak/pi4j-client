package org.kaloz.pi4j.remote.server

import com.typesafe.config.ConfigFactory

trait Configuration {

  lazy val config = ConfigFactory.load("pi4j-remote-server")
}
