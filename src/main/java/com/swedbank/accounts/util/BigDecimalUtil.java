package com.swedbank.accounts.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
  private BigDecimalUtil() {}

  public static BigDecimal bd(String bdString) {
    return new BigDecimal(bdString);
  }
}
