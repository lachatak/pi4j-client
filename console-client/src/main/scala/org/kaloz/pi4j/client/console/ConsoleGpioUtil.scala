package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.GpioUtil
import org.kaloz.pi4j.client.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.client.messages.ClientMessages.PinEdge._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpioUtil(stubClientActor: ActorRef) extends GpioUtil {

  implicit val timeout = Timeout(1 minutes)

  override def export(pin: Int, direction: Int): Unit = stubClientActor ! ExportCommand(pin, direction)

  override def unexport(pin: Int): Unit = stubClientActor ! UnexportCommand(pin)

  override def isExported(pin: Int): Boolean = Await.result((stubClientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 1 minute).exported

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((stubClientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 1 minute).status

  override def getDirection(pin: Int): Int = Await.result((stubClientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 1 minute).direction

  override def isPinSupported(pin: Int): Int = Await.result((stubClientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 1 minute).supported
}
