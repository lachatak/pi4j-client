package org.kaloz.pi4j.client.console

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.GpioUtil
import org.kaloz.pi4j.common.messages.ClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.common.messages.ClientMessages.PinDirection._
import org.kaloz.pi4j.common.messages.ClientMessages.PinEdge._

import scala.concurrent.Await
import scala.concurrent.duration._

class ConsoleGpioUtil(consoleClientActor: ActorRef) extends GpioUtil {

  implicit val timeout = Timeout(1 minutes)

  override def export(pin: Int, direction: Int): Unit = consoleClientActor ! ExportCommand(pin, direction)

  override def unexport(pin: Int): Unit = consoleClientActor ! UnexportCommand(pin)

  override def isExported(pin: Int): Boolean = Await.result((consoleClientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 1 minute).exported

  override def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((consoleClientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 1 minute).status

  override def getDirection(pin: Int): Int = Await.result((consoleClientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 1 minute).direction

  override def isPinSupported(pin: Int): Int = Await.result((consoleClientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 1 minute).supported
}
