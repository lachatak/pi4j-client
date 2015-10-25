package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kaloz.pi4j.client.GpioUtil;
import org.kaloz.pi4j.client.factory.AbstractClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class GpioUtilAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final GpioUtil gpioUtil;

    public GpioUtilAspect() {
        this.gpioUtil = AbstractClientFactory.gpioUtil();
        logger.debug("Initialised...");
    }

    @Around(value = "call (public int com.pi4j.wiringpi.GpioUtil.isPinSupported(int)) && args(pin)")
    public int isPinSupported(ProceedingJoinPoint point, int pin) {
        logger.debug("GpioUtil.isPinSupported is called with {}", pin);
        return gpioUtil.isPinSupported(pin);
    }

    @Around(value = "call (public boolean com.pi4j.wiringpi.GpioUtil.isExported(int)) && args(pin)")
    public boolean isExported(ProceedingJoinPoint point, int pin) {
        logger.debug("GpioUtil.isExported is called with {}", pin);
        return gpioUtil.isExported(pin);
    }

    @Around(value = "call (public void com.pi4j.wiringpi.GpioUtil.export(int, int)) && args(pin, direction)")
    public void export(ProceedingJoinPoint point, int pin, int direction) {
        logger.debug("GpioUtil.export is called with {} - {}", pin, direction);
        gpioUtil.export(pin, direction);
    }

    @Around(value = "call (public void com.pi4j.wiringpi.GpioUtil.unexport(int)) && args(pin)")
    public void unexport(ProceedingJoinPoint point, int pin) {
        logger.debug("GpioUtil.unexport is called with {}", pin);
        gpioUtil.unexport(pin);
    }

    @Around(value = "call (public boolean com.pi4j.wiringpi.GpioUtil.setEdgeDetection(int, int)) && args(pin, edge)")
    public boolean setEdgeDetection(ProceedingJoinPoint point, int pin, int edge) {
        logger.debug("GpioUtil.setEdgeDetection is called with {} - {}", pin, edge);
        return gpioUtil.setEdgeDetection(pin, edge);
    }

    @Around(value = "call (public int com.pi4j.wiringpi.GpioUtil.getDirection(int)) && args(pin, edge)")
    public int getDirection(ProceedingJoinPoint point, int pin, int edge) {
        logger.debug("GpioUtil.getDirection is called with {} - {}", pin, edge);
        return gpioUtil.getDirection(pin);
    }
}
