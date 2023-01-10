package com.cherrysoft.ahorrosapp.web.logging.aspect.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.web.utils.ToStringUtils.toJsonString;
import static java.util.Objects.isNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingUtils {

  public static Object logMethodAround(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch timer = new StopWatch();
    timer.start();
    Object proceed = joinPoint.proceed();
    timer.stop();
    String methodWithClassName = getMethodWithClassName(joinPoint);
    var argsWithNames = getArgsWithNames(joinPoint);
    long totalTimeMillis = timer.getTotalTimeMillis();
    var toLog = Map.of("method", methodWithClassName, "args", argsWithNames, "executionTime", totalTimeMillis);
    log.debug(toJsonString(toLog));
    return proceed;
  }

  public static void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
    String methodWithClassName = getMethodWithClassName(joinPoint);
    Map<String, String> argsWithNames = getArgsWithNames(joinPoint);
    var entriesToLog = Map.of("method", methodWithClassName, "args", argsWithNames);
    log.error(toJsonString(entriesToLog), exception);
  }

  private static String getMethodWithClassName(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    return String.format("%s.%s", className, methodName);
  }

  private static Map<String, String> getArgsWithNames(JoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String[] paramNames = methodSignature.getParameterNames();
    Object[] args = joinPoint.getArgs();
    Map<String, String> result = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      String paramName = paramNames[i];
      Object arg = args[i];
      result.put(paramName, isNull(arg) ? "null" : arg.toString());
    }
    return result;
  }

}
