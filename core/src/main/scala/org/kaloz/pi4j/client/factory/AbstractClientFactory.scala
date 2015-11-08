package org.kaloz.pi4j.client.factory

import com.typesafe.scalalogging.StrictLogging
import org.kaloz.pi4j.client.fallback.FallbackClientFactory
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}
import org.reflections.Reflections

import scala.collection.JavaConversions._

private[factory] class AbstractClientFactory extends ClientFactory with StrictLogging {

  private val ClientFactoryNamingConventionPattern = "(.*)ClientFactory".r

  lazy val factory: ClientFactory = {

    val packageToScan = System.getProperty(AbstractClientFactory.pi4jClientScanPackage, "org.kaloz.pi4j.client")
    logger.debug(s"packageToScan for clients --> $packageToScan")

    val factoryClasses = new Reflections(packageToScan).getSubTypesOf(classOf[ClientFactory])
      .filterNot(info => info.getName == this.getClass.getName || info.getName == classOf[FallbackClientFactory].getName)
      .map(info => Class.forName(info.getName))
      .toList

    factoryClasses.size match {
      case 0 => throw new ClientFactoryException("No client library dependency were provided at runtime!")
      case 1 =>
        factoryClasses.head.getSimpleName match {
          case ClientFactoryNamingConventionPattern(name) =>
            logger.info(s"Client mode '$name' is initialised!")
            factoryClasses.head.getMethod("instance").invoke(null).asInstanceOf[ClientFactory]
        }
      case _ =>
        val factoriesMap: Map[String, Class[_]] = factoryClasses.collect {
          case clazz => clazz.getSimpleName match {
            case ClientFactoryNamingConventionPattern(name) => name.toLowerCase -> clazz
          }
        }.toMap

        val requestedMode = Option(System.getProperty(AbstractClientFactory.pi4jClientMode))
        val mode: String = requestedMode.fold {
          logger.warn(s"Multiple client is available on the classpath and pi4j.client.mode is not provided. Trying to initialise with default 'console' mode!")
          "console"
        }(identity)

        logger.info(s"Initialising client mode '$mode'")

        factoriesMap.get(mode).getOrElse {
          logger.warn(s"Requested client mode '$mode' is not available! No client will be used!!")
          classOf[FallbackClientFactory]
        }.getMethod("instance").invoke(null).asInstanceOf[ClientFactory]
    }
  }

  lazy val gpio: Gpio = factory.gpio
  lazy val gpioUtil: GpioUtil = factory.gpioUtil
  lazy val gpioInterrupt: GpioInterrupt = factory.gpioInterrupt

  def shutdown(): Unit = factory.shutdown
}

object AbstractClientFactory {

  val pi4jClientScanPackage = "pi4j.client.scan.package"
  val pi4jClientMode = "pi4j.client.mode"

  val instance = new AbstractClientFactory()
}

class ClientFactoryException(message: String) extends RuntimeException(message)
