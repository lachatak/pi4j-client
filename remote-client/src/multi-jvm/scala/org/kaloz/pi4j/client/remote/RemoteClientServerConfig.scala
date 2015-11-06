package org.kaloz.pi4j.client.remote

import akka.remote.testkit.MultiNodeConfig
import com.typesafe.config.ConfigFactory

object RemoteClientServerConfig extends MultiNodeConfig {

  val server = role("server")
  val client = role("client")

  nodeConfig(server)(confWithRoles("server"))
  nodeConfig(client)(confWithRoles("client"))

  commonConfig(ConfigFactory.parseString(
    """
      |akka {
      |  loggers = ["akka.event.slf4j.Slf4jLogger", "akka.testkit.TestEventListener"]
      |  loglevel = "DEBUG"
      |  akka.remote.log-remote-lifecycle-events = off
      |  actor.provider = "akka.cluster.ClusterActorRefProvider"
      |  extensions = ["akka.cluster.pubsub.DistributedPubSub"]
      |  cluster {
      |       auto-down-unreachable-after = 0s
      |       pub-sub.max-delta-elements = 500
      |       metrics.enabled=off
      |  }
      |  test.filter-leeway = 6s
      |}
    """.stripMargin))

  def confWithRoles(roles: String*) = ConfigFactory.parseString(s"akka.cluster.roles=[${roles.mkString(",")}]")

}
