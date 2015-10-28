package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kaloz.pi4j.client.Gpio;
import org.kaloz.pi4j.client.factory.AbstractClientFactory;
import org.kaloz.pi4j.client.factory.ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Aspect
public class GpioAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final ClientFactory clientFactory;
    private final Gpio gpio;
    private final AtomicBoolean shutDown = new AtomicBoolean(false);

    public GpioAspect() {
        this.clientFactory = AbstractClientFactory.instance();
        this.gpio = clientFactory.gpio();
        logger.debug("Initialised...");
    }

    @Around(value = "staticinitialization(com.pi4j.wiringpi.Gpio)")
    public void staticinitialization(ProceedingJoinPoint point) {
        logger.info("Skipping Gpio static init block...");
    }

    @Around(value = "call (public int com.pi4j.wiringpi.Gpio.wiringPiSetup())")
    public int wiringPiSetup(ProceedingJoinPoint point) {
        logger.debug("Gpio.wiringPiSetup is called!");
        return gpio.wiringPiSetup();
    }

    @Around(value = "call (public void com.pi4j.wiringpi.Gpio.pinMode(int, int)) && args(pin, mode)")
    public void pinMode(ProceedingJoinPoint point, int pin, int mode) {
        logger.debug("Gpio.pinMode is called with {}, {}", pin, mode);
        gpio.pinMode(pin, mode);
    }

    @Around(value = "call (public void com.pi4j.wiringpi.Gpio.digitalWrite(int, int)) && args(pin, value)")
    public void digitalWrite(ProceedingJoinPoint point, int pin, int value) {
        logger.debug("Gpio.digitalWrite is called with {}, {}", pin, value);
        gpio.digitalWrite(pin, value);
    }

    @Around(value = "call (public int com.pi4j.wiringpi.Gpio.digitalRead(int)) && args(pin)")
    public int digitalRead(ProceedingJoinPoint point, int pin) {
        logger.debug("Gpio.digitalRead is called with {}", pin);
        return gpio.digitalRead(pin);
    }

    @Around(value = "call (public void com.pi4j.wiringpi.Gpio.pullUpDnControl(int, int)) && args(pin, pud)")
    public void pullUpDnControl(ProceedingJoinPoint point, int pin, int pud) {
        logger.debug("Gpio.pullUpDnControl is called with {}, {}", pin, pud);
        gpio.pullUpDnControl(pin, pud);
    }

    @Around(value = "call (public void com.pi4j.wiringpi.Gpio.pwmWrite(int, int)) && args(pin, value)")
    public void pwmWrite(ProceedingJoinPoint point, int pin, int value) {
        logger.debug("Gpio.pwmWrite is called with {}, {}", pin, value);
        gpio.pwmWrite(pin, value);
    }

    @Around(value = "execution (public void com.pi4j.io.gpio.GpioController.shutdown())")
    public void shutdown(ProceedingJoinPoint point) throws Throwable {
        if (shutDown.weakCompareAndSet(false, true)) {
            logger.debug("GpioController.shutdown is called");
            point.proceed();
            clientFactory.shutdown();
        }
    }
}
