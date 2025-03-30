package com.swedbank.accounts.config;

import com.swedbank.accounts.dto.ECurrency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static com.swedbank.accounts.dto.ECurrency.*;
import static com.swedbank.accounts.util.BigDecimalUtil.bd;

public class ExchangeRatesConfig {
  private ExchangeRatesConfig() {}

  private static final Map<ECurrency, Map<ECurrency, BigDecimal>> RATES = Map.of(
    EUR, Map.of(
      USD, bd("1.08"),
      DKK, bd("7.45"),
      GBP, bd("0.85"),
      JPY, bd("160.00"),
      CHF, bd("0.98")
    ),
    USD, Map.of(
      EUR, bd("0.93"),
      DKK, bd("6.89"),
      GBP, bd("0.79"),
      JPY, bd("148.00"),
      CHF, bd("0.91")
    ),
    DKK, Map.of(
      EUR, bd("0.13"),
      USD, bd("0.15"),
      GBP, bd("0.11"),
      JPY, bd("21.50"),
      CHF, bd("0.13")
    ),
    GBP, Map.of(
      EUR, bd("1.18"),
      USD, bd("1.26"),
      DKK, bd("8.67"),
      JPY, bd("187.00"),
      CHF, bd("1.15")
    ),
    JPY, Map.of(
      EUR, bd("0.0062"),
      USD, bd("0.0068"),
      DKK, bd("0.046"),
      GBP, bd("0.0053"),
      CHF, bd("0.0061")
    ),
    CHF, Map.of(
      EUR, bd("1.02"),
      USD, bd("1.10"),
      DKK, bd("7.78"),
      GBP, bd("0.87"),
      JPY, bd("163.93")
    )
  );

  public static Optional<BigDecimal> getExchangeRate(ECurrency from, ECurrency to) {
    return Optional.ofNullable(RATES.get(from))
      .map(it -> it.get(to));
  }
}
