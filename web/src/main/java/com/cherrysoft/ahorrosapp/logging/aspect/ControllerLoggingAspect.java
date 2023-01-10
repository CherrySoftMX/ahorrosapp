package com.cherrysoft.ahorrosapp.logging.aspect;

import com.cherrysoft.ahorrosapp.logging.aspect.utils.LoggingUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

  @Around("within(com.cherrysoft.ahorrosapp.controllers..*)")
  public Object logAroundMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
    return LoggingUtils.logMethodAround(joinPoint);
  }

}
