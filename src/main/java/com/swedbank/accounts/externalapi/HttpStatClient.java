package com.swedbank.accounts.externalapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

@Component
@Slf4j
public class HttpStatClient {

  private final String httpStatUrl;
  private final RestTemplate restTemplate;

  public HttpStatClient(@Value("${httpStat.url}") String httpStatUrl,
                        RestTemplate restTemplate) {
    this.httpStatUrl = httpStatUrl;
    this.restTemplate = restTemplate;
  }

  public HttpStatusCode get200Response() {
    String uri = UriComponentsBuilder.fromUriString(httpStatUrl)
      .pathSegment("200")
      .build()
      .toUriString();

    log.info("Making external request to {}", uri);
    if (isRandomIntegerDivisibleByThree()) {
      simulateExternalApiFailure(uri);
    }

    try {
      var response = restTemplate.getForEntity(uri, String.class);
      log.info("Received response: {}", response);
      return response.getStatusCode();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean isRandomIntegerDivisibleByThree() {
    return new Random().nextInt() % 3 == 0;
  }

  private static void simulateExternalApiFailure(String uri) {
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(uri + " is unreachable, try again later");
  }
}
