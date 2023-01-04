package com.cherrysoft.ahorrosapp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationTimeUtils {

  public static LocalDateTime getLocalDateTime() {
    return LocalDateTime.now();
  }

  public static String getTimeString() {
    final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    return getLocalDateTime().format(format);
  }

}
