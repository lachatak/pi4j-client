package org.kaloz.pi4j.client

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.common.messages.ClientMessages.PinEdge._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioUtilActorGateway(clientActor: ActorRef) extends GpioUtil {

  implicit val timeout = Timeout(5 seconds)

  override def export(pin: Int, direction: Int): Unit = clientActor ! ExportCommand(pin, direction)

  override def unexport(pin: Int): Unit = clientActor ! UnexportCommand(pin)

  override def isExported(pin: Int): Boolean = Await.result((clientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 5 seconds).exported

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((clientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 5 seconds).status

  override def getDirection(pin: Int): Int = Await.result((clientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 5 seconds).direction

  override def isPinSupported(pin: Int): Int = Await.result((clientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 5 seconds).supported
}
