package org.kaloz.pi4j.client.console

import com.typesafe.config.ConfigFactory

trait Configuration {

  private lazy val config = ConfigFactory.load("pi4j-client-stub")

  private lazy val pinKeyConfig = config.getConfig("stub.input.key")

  def keyMap(pin: Int) = pinKeyConfig.getString(s"pin$pin").charAt(0).toUpper
}
