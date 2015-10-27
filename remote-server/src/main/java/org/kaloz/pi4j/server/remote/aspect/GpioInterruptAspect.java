package org.kaloz.pi4j.server.remote.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kaloz.pi4j.server.remote.Main.pinStateChangeCallback;

@Aspect
public class GpioInterruptAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public GpioInterruptAspect() {
        logger.debug("Initialised...");
    }

    @Around(value = "execution (private void com.pi4j.wiringpi.GpioInterrupt.pinStateChangeCallback(int, boolean)) && args(pin, state)")
    public void pinStateChangeCallbackJoinPoint(ProceedingJoinPoint point, int pin, boolean state) {
        logger.debug("GpioInterrupt.pinStateChangeCallback is called with {} {}", pin, state);
        pinStateChangeCallback(pin, state);
    }

}
