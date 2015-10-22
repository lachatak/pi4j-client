package org.kaloz.pi4j.client.factory

import com.typesafe.config.ConfigFactory

trait Configuration {

  private lazy val config = ConfigFactory.load("pi4j-core")

  private lazy val clientClasses = config.getConfig("client.class")

  def classByType(clientType: String) = clientClasses.getString(clientType)
}
