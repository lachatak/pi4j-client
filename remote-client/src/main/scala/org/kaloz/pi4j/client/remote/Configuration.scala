package org.kaloz.pi4j.client.remote

import com.typesafe.config.ConfigFactory

trait Configuration {

  lazy val config = ConfigFactory.load("pi4j-remote-client")
}
