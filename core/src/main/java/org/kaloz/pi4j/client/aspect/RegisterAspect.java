package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Aspect
public class RegisterAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Set<String> points = new HashSet<>();

    public RegisterAspect() {
        logger.debug("Initialised...");
    }

    @Before(value = "call (public * com.pi4j.wiringpi.Gpio.*(..))")
    public void logGpio(JoinPoint point) {
        logger.debug(String.format("pointcut: %-20s join point: %s", "Gpio Call", point.toShortString()));
        points.add(point.toShortString());
    }

    @Before(value = "call (public * com.pi4j.wiringpi.GpioUtil.*(..))")
    public void logGpioUtil(JoinPoint point) {
        logger.debug(String.format("pointcut: %-20s join point: %s", "GpioUtil Call", point.toShortString()));
        points.add(point.toShortString());
    }

    @Before(value = "call (public * com.pi4j.wiringpi.GpioInterrupt.*(..))")
    public void logGpioInterrupt(JoinPoint point) {
        logger.debug(String.format("pointcut: %-20s join point: %s", " GpioInterrupt Call", point.toShortString()));
        points.add(point.toShortString());
    }

    @Before(value = "call (public void com.pi4j.io.gpio.impl.GpioControllerImpl.shutdown())")
    public void shutdown(JoinPoint point) {
        logger.info("Calls: {}", points.toString());
    }
}
