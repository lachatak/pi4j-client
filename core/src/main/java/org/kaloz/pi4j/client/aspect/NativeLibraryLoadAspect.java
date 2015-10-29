package org.kaloz.pi4j.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class NativeLibraryLoadAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public NativeLibraryLoadAspect() {
        logger.debug("Initialised...");
    }

    @Around(value = "call (public void com.pi4j.util.NativeLibraryLoader.load(String)) && args(library)")
    public void load(ProceedingJoinPoint point, String library) {
        logger.debug("NativeLibraryLoader.load is SKIPPED for {}", library);
    }

}
