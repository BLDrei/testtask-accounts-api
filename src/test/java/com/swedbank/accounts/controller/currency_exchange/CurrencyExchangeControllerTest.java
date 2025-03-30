package com.swedbank.accounts.controller.currency_exchange;

import com.swedbank.accounts.controller.AbstractControllerTest;
import com.swedbank.accounts.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyExchangeControllerTest extends AbstractControllerTest {

  @Test
  public void testCurrencyConversion_EURtoUSD_success() {
    String from = "EUR";
    String to = "USD";
    BigDecimal amount = new BigDecimal("20");

    URI uri = UriComponentsBuilder.fromPath("/currency-exchange/convert")
      .queryParam("fromCurrency", from)
      .queryParam("targetCurrency", to)
      .queryParam("amount", amount)
      .build()
      .toUri();

    ResponseEntity<Double> response = restTemplate.getForEntity(uri, Double.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    assertEquals(21.60, response.getBody());
  }

  @Test
  public void testCurrencyConversion_EURtoEURhasNoExchangeRate_shouldFail() {
    String from = "EUR";
    String to = "EUR";
    BigDecimal amount = new BigDecimal("20");

    URI uri = UriComponentsBuilder.fromPath("/currency-exchange/convert")
      .queryParam("fromCurrency", from)
      .queryParam("targetCurrency", to)
      .queryParam("amount", amount)
      .build()
      .toUri();

    ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(uri, ErrorResponse.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Could not find exchange rate from EUR to EUR", response.getBody().message());
  }
}
