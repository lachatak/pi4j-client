package org.kaloz.pi4j.client.console

import com.typesafe.config.ConfigFactory

trait Configuration {

  private lazy val config = ConfigFactory.load("pi4j-console-client")

  private lazy val pinKeyConfig = config.getConfig("console.input.key")

  def keyMap(pin: Int) = pinKeyConfig.getString(s"pin$pin").charAt(0).toUpper
}
