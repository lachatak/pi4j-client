package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.kaloz.pi4j.client.GpioInterrupt;
import org.kaloz.pi4j.client.factory.AbstractClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class GpioInterruptAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final GpioInterrupt gpioInterrupt;

    public GpioInterruptAspect() {
        this.gpioInterrupt = AbstractClientFactory.instance().gpioInterrupt();
        logger.debug("Initialised...");
    }

//    @Around(value = "staticinitialization(com.pi4j.wiringpi.GpioInterrupt)")
//    public void staticinitialization(ProceedingJoinPoint point) {
//        logger.info("Skipping GpioInterrupt static init block...");
//    }
//
//    @Before(value = "get(* com.pi4j.wiringpi.GpioInterrupt.listeners)")
//    public void test(JoinPoint point) {
//        logger.info("--------------------- Vector is called!!");
//    }

    @Around(value = "call (public int com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(int)) && args(pin)")
    public int enablePinStateChangeCallback(ProceedingJoinPoint point, int pin) {
        logger.debug("GpioInterrupt.enablePinStateChangeCallback is called with {}", pin);
        return gpioInterrupt.enablePinStateChangeCallback(pin);
    }

    @Around(value = "call (public int com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(int)) && args(pin)")
    public int disablePinStateChangeCallback(ProceedingJoinPoint point, int pin) {
        logger.debug("GpioInterrupt.disablePinStateChangeCallback is called with {}", pin);
        return gpioInterrupt.disablePinStateChangeCallback(pin);
    }

}
