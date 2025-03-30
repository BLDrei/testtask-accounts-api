package com.swedbank.accounts.config;

import java.math.BigDecimal;

public class AccountConfig {
  private AccountConfig() {}

  public static BigDecimal MIN_BALANCE_AMOUNT = BigDecimal.ZERO;
  public static BigDecimal MAX_BALANCE_AMOUNT = new BigDecimal("99999999999.99");

}
