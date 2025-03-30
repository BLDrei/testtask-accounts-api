package com.swedbank.accounts.dto;

import java.math.BigDecimal;

public record AccountBalanceDto(
  BigDecimal amount,
  String currencyCode,
  String currencySign
) {}
