package org.kaloz.pi4j.client.remote

import akka.actor.{ActorPath, ActorSystem}
import akka.cluster.Cluster
import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.factory.ClientFactory
import org.kaloz.pi4j.client.{GpioActorGateway, GpioInterruptActorGateway, GpioUtilActorGateway}
import org.kaloz.pi4j.common.messages.ClientMessages.ControlMessages.Shutdown

import scala.collection.JavaConversions._

class RemoteClientFactory extends ClientFactory with Configuration with StrictLogging {

  logger.info("Initializing...")

  private val system = ActorSystem("pi4j-remoting", config)

  private val seedNode = config.getStringList("akka.cluster.seed-nodes").head

  private val remoteServerActor = system.actorSelection(ActorPath.fromString(s"$seedNode/user/remoteServerActor"))
  private val remoteClientActor = system.actorOf(RemoteClientActor.props(remoteServerActor), "remoteClientActor")

  system.actorOf(RemoteInputPinStateChangedListenerActor.props(), "pinStateChangedListenerActor")

  val gpio = new GpioActorGateway(remoteClientActor)
  val gpioUtil = new GpioUtilActorGateway(remoteClientActor)
  val gpioInterrupt = new GpioInterruptActorGateway(remoteClientActor)

  def shutdown: Unit = {
    remoteServerActor ! Shutdown
    val cluster = Cluster(system)
    cluster.leave(cluster.selfAddress)
    system.terminate()
  }
}

object RemoteClientFactory {
  val instance = new RemoteClientFactory()
}
