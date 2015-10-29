package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kaloz.pi4j.client.factory.AbstractClientFactory;
import org.kaloz.pi4j.client.factory.ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Aspect
public class GpioControllerAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final ClientFactory clientFactory;
    private final AtomicBoolean shutDown = new AtomicBoolean(false);

    public GpioControllerAspect() {
        this.clientFactory = AbstractClientFactory.instance();
        logger.debug("Initialised...");
    }

    @Around(value = "execution (public void com.pi4j.io.gpio.impl.GpioControllerImpl.shutdown())")
    public void shutdown(ProceedingJoinPoint point) throws Throwable {
        if (shutDown.weakCompareAndSet(false, true)) {
            logger.debug("GpioController.shutdown is called");
            point.proceed();
            clientFactory.shutdown();
        }
    }
}
