package org.kaloz.pi4j.remote.server

import akka.actor.ActorSystem
import akka.cluster.Cluster

object Main extends App with Configuration {

  val system = ActorSystem("Pi4jRemoting", config)
  val joinAddress = Cluster(system).selfAddress
  Cluster(system).join(joinAddress)

}
