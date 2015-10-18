package org.kaloz.pi4j.client.stub

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.kaloz.pi4j.client.GpioUtil
import org.kaloz.pi4j.client.common.Pi4jClientMessages.GpioUtilMessages._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinDirection._
import org.kaloz.pi4j.client.common.Pi4jClientMessages.PinEdge._
import org.kaloz.pi4j.client.common.Pi4jClientMessages._

import scala.concurrent.Await
import scala.concurrent.duration._

class GpioUtilImpl(stubClientActor: ActorRef) extends GpioUtil {

  implicit val timeout = Timeout(1 minutes)

  @throws(classOf[RuntimeException])
  def export(pin: Int, direction: Int): Unit = Await.result((stubClientActor ? ExportCommand(pin, direction)).mapTo[Done.type], 1 minute)

  @throws(classOf[RuntimeException])
  def unexport(pin: Int): Unit = Await.result((stubClientActor ? UnexportCommand(pin)).mapTo[Done.type], 1 minute)

  @throws(classOf[RuntimeException])
  def isExported(pin: Int): Boolean = Await.result((stubClientActor ? IsExportedRequest(pin)).mapTo[IsExportedResponse], 1 minute).exported

  @throws(classOf[RuntimeException])
  def setEdgeDetection(pin: Int, edge: Int): Boolean = Await.result((stubClientActor ? SetEdgeDetectionRequest(pin, edge)).mapTo[SetEdgeDetectionResponse], 1 minute).status

  @throws(classOf[RuntimeException])
  def getEdgeDetection(pin: Int): Int = ???

  @throws(classOf[RuntimeException])
  def setDirection(pin: Int, direction: Int): Boolean = ???

  @throws(classOf[RuntimeException])
  def getDirection(pin: Int): Int = Await.result((stubClientActor ? GetDirectionRequest(pin)).mapTo[GetDirectionReponse], 1 minute).direction

  @throws(classOf[RuntimeException])
  def isPinSupported(pin: Int): Int = Await.result((stubClientActor ? IsPinSupportedRequest(pin)).mapTo[IsPinSupportedResponse], 1 minute).supported
}
