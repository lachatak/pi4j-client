package org.kaloz.pi4j.server.remote.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kaloz.pi4j.server.remote.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class GpioInterruptAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public GpioInterruptAspect() {
        logger.debug("Initialised...");
    }

    @Around(value = "execution (private void com.pi4j.wiringpi.GpioInterrupt.pinStateChangeCallback(int, boolean)) && args(pin, state)")
    public void pinStateChangeCallback(ProceedingJoinPoint point, int pin, boolean state) {
        logger.debug("GpioInterrupt.pinStateChangeCallback is called with {} {}", pin, state);
        //TODO remove this call from Main. Find a better place
        Main.pinStateChangeCallback(pin, state);
    }

}
