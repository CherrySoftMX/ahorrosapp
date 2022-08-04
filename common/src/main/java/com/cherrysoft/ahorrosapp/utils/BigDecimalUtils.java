package com.cherrysoft.ahorrosapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

  public static BigDecimal divide(BigDecimal total, Integer count) {
    return total.divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
  }

}
