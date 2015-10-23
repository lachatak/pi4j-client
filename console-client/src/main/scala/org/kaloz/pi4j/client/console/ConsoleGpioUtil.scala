package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import org.kaloz.pi4j.client.GpioUtil
import org.kaloz.pi4j.client.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.client.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.client.messages.ClientMessages.PinEdge._
import org.kaloz.pi4j.client.messages.ClientMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpioUtil(stubClientActor: ActorRef) extends GpioUtil {

  override def export(pin: Int, direction: Int): Unit = Await.result((stubClientActor ? ExportCommand(pin, direction)).mapTo[Done.type], 1 minute)

  override def unexport(pin: Int): Unit = Await.result((stubClientActor ? UnexportCommand(pin)).mapTo[Done.type], 1 minute)

  override def isExported(pin: Int): Boolean = Await.result((stubClientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 1 minute).exported

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((stubClientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 1 minute).status

  override def getDirection(pin: Int): Int = Await.result((stubClientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 1 minute).direction

  override def isPinSupported(pin: Int): Int = Await.result((stubClientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 1 minute).supported
}
