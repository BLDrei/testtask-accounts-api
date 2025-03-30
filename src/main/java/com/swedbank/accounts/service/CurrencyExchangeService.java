package com.swedbank.accounts.service;

import com.swedbank.accounts.config.ExchangeRatesConfig;
import com.swedbank.accounts.dto.ECurrency;
import com.swedbank.accounts.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyExchangeService {

  public BigDecimal convert(ECurrency from, ECurrency to, BigDecimal amount) {
    BigDecimal rate = ExchangeRatesConfig.getExchangeRate(from, to)
      .orElseThrow(() -> new NotFoundException("Could not find exchange rate from %s to %s".formatted(from, to)));

    return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
  }
}
