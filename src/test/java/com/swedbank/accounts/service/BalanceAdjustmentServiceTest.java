package com.swedbank.accounts.service;

import com.swedbank.accounts.dto.ECurrency;
import com.swedbank.accounts.dto.balance_adjustment.BalanceAdjustmentDto;
import com.swedbank.accounts.entity.Account;
import com.swedbank.accounts.entity.AccountBalance;
import com.swedbank.accounts.exception.BusinessException;
import com.swedbank.accounts.exception.NotFoundException;
import com.swedbank.accounts.externalapi.HttpStatClient;
import com.swedbank.accounts.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.swedbank.accounts.util.BigDecimalUtil.bd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Sql(scripts = {"/db/clean_up_accounts.sql", "/db/add_sample_accounts.sql"})
@Transactional
class BalanceAdjustmentServiceTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private AccountRepository accountRepository;

  private BalanceAdjustmentService balanceAdjustmentService;

  @BeforeEach
  public void setUp() {
    HttpStatClient httpStatClient = mock(HttpStatClient.class);

    balanceAdjustmentService = new BalanceAdjustmentService(
      accountRepository,
      httpStatClient
    );

    lenient().when(httpStatClient.get200Response()).thenReturn(HttpStatusCode.valueOf(200));
  }


  @Test
  void addMoney_happyFlow() {
    balanceAdjustmentService.addMoneyToAccount("1234567890", List.of(
      new BalanceAdjustmentDto(bd("10"), ECurrency.EUR),
      new BalanceAdjustmentDto(bd("24.32"), ECurrency.DKK)
    ));
    entityManager.flush();

    var account = accountRepository.findAccountByAccountNumber("1234567890").get();

    assertEquals(2, account.getAccountBalances().size());

    var eurBalance = findAccountBalanceByCurrencyCode(account, ECurrency.EUR).get();
    var dkkBalance = findAccountBalanceByCurrencyCode(account, ECurrency.DKK).get();

    assertThat(eurBalance.getAmount()).isEqualByComparingTo(bd("1510"));
    assertThat(dkkBalance.getAmount()).isEqualByComparingTo(bd("104.44"));
  }

  @Test
  void addMoney_toBlockedAccount_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.addMoneyToAccount("XXXXXXXXXX", List.of(
        new BalanceAdjustmentDto(bd("10"), ECurrency.EUR)
      ))
    );
  }

  @Test
  void addMoney_oneModificationExceedsMaximum_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.addMoneyToAccount("1234567890", List.of(
        new BalanceAdjustmentDto(bd("5000"), ECurrency.EUR),
        new BalanceAdjustmentDto(bd("99999999999"), ECurrency.DKK)
      ))
    );
  }

  @Test
  void addMoney_oneModificationIsOfUnsupportedCurrency_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.addMoneyToAccount("1234567890", List.of(
        new BalanceAdjustmentDto(bd("5000"), ECurrency.EUR),
        new BalanceAdjustmentDto(bd("2"), ECurrency.GBP)
      ))
    );
  }

  @Test
  void addMoney_accountNotFound_shouldFail() {
    assertThrows(
      NotFoundException.class,
      () -> balanceAdjustmentService.addMoneyToAccount("not_existing_accountNumber", List.of(
        new BalanceAdjustmentDto(bd("5000"), ECurrency.EUR)
      ))
    );
  }


  @Test
  void debitMoney_happyFlow() {
    balanceAdjustmentService.debitMoneyFromAccount(
      "1234567890",
      new BalanceAdjustmentDto(bd("10"), ECurrency.EUR)
    );
    entityManager.flush();


    var account = accountRepository.findAccountByAccountNumber("1234567890").get();

    assertEquals(2, account.getAccountBalances().size());

    var eurBalance = findAccountBalanceByCurrencyCode(account, ECurrency.EUR).get();
    var dkkBalance = findAccountBalanceByCurrencyCode(account, ECurrency.DKK).get();

    assertThat(eurBalance.getAmount()).isEqualByComparingTo(bd("1490"));
    assertThat(dkkBalance.getAmount()).isEqualByComparingTo(bd("80.12"));
  }

  @Test
  void debitMoney_fromBlockedAccount_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.debitMoneyFromAccount(
        "XXXXXXXXXX",
        new BalanceAdjustmentDto(bd("10"), ECurrency.EUR)
      )
    );
  }

  @Test
  void debitMoney_tryingToDebitMoreThanAccountHas_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.debitMoneyFromAccount(
        "1234567890",
        new BalanceAdjustmentDto(bd("1500.01"), ECurrency.EUR)
      )
    );
  }

  @Test
  void debitMoney_modificationIsOfUnsupportedCurrency_shouldFail() {
    assertThrows(
      BusinessException.class,
      () -> balanceAdjustmentService.debitMoneyFromAccount(
        "1234567890",
        new BalanceAdjustmentDto(bd("2"), ECurrency.GBP)
      )
    );
  }

  @Test
  void debitMoney_accountNotFound_shouldFail() {
    assertThrows(
      NotFoundException.class,
      () -> balanceAdjustmentService.debitMoneyFromAccount(
        "not_existing_accountNumber",
        new BalanceAdjustmentDto(bd("5000"), ECurrency.EUR)
      )
    );
  }

  private static Optional<AccountBalance> findAccountBalanceByCurrencyCode(Account account, ECurrency currency) {
    return account.getAccountBalances().stream()
      .filter(it -> it.getCurrency().getCode().equals(currency.name()))
      .findAny();
  }
}
