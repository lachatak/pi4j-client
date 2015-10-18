package org.kaloz.pi4j.client.stub

import com.typesafe.config.ConfigFactory

trait Configuration {

  val config = ConfigFactory.load("pi4j-client-stub")

  val BCM_04_key = config.getString("stub.pin.input.key.BCM_04")
  val BCM_17_key = config.getString("stub.pin.input.key.BCM_17")
  val BCM_27_key = config.getString("stub.pin.input.key.BCM_27")
  val BCM_22_key = config.getString("stub.pin.input.key.BCM_22")
  val BCM_05_key = config.getString("stub.pin.input.key.BCM_05")
  val BCM_06_key = config.getString("stub.pin.input.key.BCM_06")
  val BCM_13_key = config.getString("stub.pin.input.key.BCM_13")
  val BCM_19_key = config.getString("stub.pin.input.key.BCM_19")
  val BCM_26_key = config.getString("stub.pin.input.key.BCM_26")

  val BCM_18_key = config.getString("stub.pin.input.key.BCM_18")
  val BCM_23_key = config.getString("stub.pin.input.key.BCM_23")
  val BCM_24_key = config.getString("stub.pin.input.key.BCM_24")
  val BCM_25_key = config.getString("stub.pin.input.key.BCM_25")
  val BCM_12_key = config.getString("stub.pin.input.key.BCM_12")
  val BCM_16_key = config.getString("stub.pin.input.key.BCM_16")
  val BCM_20_key = config.getString("stub.pin.input.key.BCM_20")
  val BCM_21_key = config.getString("stub.pin.input.key.BCM_21")
}
