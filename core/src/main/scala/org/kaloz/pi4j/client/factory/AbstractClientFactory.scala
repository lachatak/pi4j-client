package org.kaloz.pi4j.client.factory

import com.typesafe.scalalogging.StrictLogging
import org.clapper.classutil.ClassFinder
import org.kaloz.pi4j.client.fallback.FallbackClientFactory
import org.kaloz.pi4j.client.{Gpio, GpioInterrupt, GpioUtil}

object AbstractClientFactory extends ClientFactory with StrictLogging {

  private val ClientFactoryNamingConventionPattern = "(.*)ClientFactory".r

  lazy val factory: ClientFactory = {

    val factoryClasses = ClassFinder.concreteSubclasses(classOf[ClientFactory].getName, ClassFinder().getClasses.iterator)
      .filterNot(info => info.name == AbstractClientFactory.getClass.getName || info.name == classOf[FallbackClientFactory].getName)
      .map(info => Class.forName(info.name))
      .toList

    factoryClasses.size match {
      case 0 => throw new ClientFactoryException("No client library dependency were provided at runtime!")
      case 1 =>
        factoryClasses.head.getSimpleName match {
          case ClientFactoryNamingConventionPattern(name) =>
            logger.info(s"Client mode '$name' is initialised!")
            factoryClasses.head.newInstance().asInstanceOf[ClientFactory]
        }
      case _ =>
        val factoriesMap: Map[String, Class[_]] = factoryClasses.collect {
          case clazz => clazz.getSimpleName match {
            case ClientFactoryNamingConventionPattern(name) => name.toLowerCase -> clazz
          }
        }.toMap

        val requestedMode = Option(System.getProperty("pi4j.client.mode"))
        val mode: String = requestedMode.fold {
          logger.warn(s"Multiple client is available on the classpath and pi4j.client.mode is not provided. Trying to initialise with default 'console' mode!")
          "console"
        }(identity)

        logger.info(s"Initialising client mode '$mode'")

        factoriesMap.get(mode).getOrElse {
          logger.warn(s"Requested client mode '$mode' is not available! No client will be used!!")
          classOf[FallbackClientFactory]
        }.newInstance().asInstanceOf[ClientFactory]
    }
  }

  lazy val gpio: Gpio = factory.gpio
  lazy val gpioUtil: GpioUtil = factory.gpioUtil
  lazy val gpioInterrupt: GpioInterrupt = factory.gpioInterrupt

  def shutdown(): Unit = factory.shutdown
}

class ClientFactoryException(message: String) extends RuntimeException(message)
