package com.swedbank.accounts.controller;

import com.swedbank.accounts.dto.ECurrency;
import com.swedbank.accounts.service.CurrencyExchangeService;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("currency-exchange")
@RequiredArgsConstructor
@Validated
public class CurrencyExchangeController {

  private final CurrencyExchangeService currencyExchangeService;

  @GetMapping("convert")
  public BigDecimal convertCurrency(@RequestParam ECurrency fromCurrency,
                                    @RequestParam ECurrency targetCurrency,
                                    @RequestParam @Positive @Digits(integer = 11, fraction = 2) BigDecimal amount) {
    return currencyExchangeService.convert(fromCurrency, targetCurrency, amount);
  }
}
