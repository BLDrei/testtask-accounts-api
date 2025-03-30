package com.swedbank.accounts.dto.balance_adjustment;

import com.swedbank.accounts.dto.ECurrency;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BalanceAdjustmentDto(
  @Positive
  @Digits(integer = 11, fraction = 2)
  BigDecimal amount,

  @NotNull
  ECurrency currency
) {}
