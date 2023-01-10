package com.cherrysoft.ahorrosapp.web.logging.aspect;

import com.cherrysoft.ahorrosapp.web.logging.aspect.utils.LoggingUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {

  @Around("within(com.cherrysoft.ahorrosapp.repositories..*)")
  public Object logAroundMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
    return LoggingUtils.logMethodAround(joinPoint);
  }

  @AfterThrowing(
      pointcut = "within(com.cherrysoft.ahorrosapp.repositories..*)",
      throwing = "exception"
  )
  public void logAfterThrowing(final JoinPoint joinPoint, final Throwable exception) {
    LoggingUtils.logAfterThrowing(joinPoint, exception);
  }

}
