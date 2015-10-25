package org.kaloz.pi4j.client.remote

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.GpioUtil
import org.kaloz.pi4j.client.messages.ClientMessages.GpioUtilMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class RemoteClientGpioUtil(remoteClientActor: ActorRef) extends GpioUtil {

  implicit val timeout = Timeout(1 minutes)

  override def export(pin: Int, direction: Int): Unit = remoteClientActor ! ExportCommand(pin, direction)

  override def unexport(pin: Int): Unit = remoteClientActor ! UnexportCommand(pin)

  override def isExported(pin: Int): Boolean = Await.result((remoteClientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 1 minute).exported

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((remoteClientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 1 minute).status

  override def getDirection(pin: Int): Int = Await.result((remoteClientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 1 minute).direction

  override def isPinSupported(pin: Int): Int = Await.result((remoteClientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 1 minute).supported
}
