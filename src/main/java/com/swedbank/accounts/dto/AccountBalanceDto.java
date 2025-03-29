package com.swedbank.accounts.dto;

import java.math.BigDecimal;

public record AccountBalanceDto(
  BigDecimal balance,
  String currencyCode,
  String currencySign
) {}
