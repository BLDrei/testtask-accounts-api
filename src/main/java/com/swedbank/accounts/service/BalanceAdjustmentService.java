package com.swedbank.accounts.service;

import com.swedbank.accounts.config.AccountConfig;
import com.swedbank.accounts.dto.EAccountStatus;
import com.swedbank.accounts.dto.ECurrency;
import com.swedbank.accounts.dto.balance_adjustment.BalanceAdjustmentDto;
import com.swedbank.accounts.entity.AccountBalance;
import com.swedbank.accounts.entity.AccountStatus;
import com.swedbank.accounts.exception.BusinessException;
import com.swedbank.accounts.exception.NotFoundException;
import com.swedbank.accounts.externalapi.HttpStatClient;
import com.swedbank.accounts.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.swedbank.accounts.util.CompareUtil.greaterThan;
import static com.swedbank.accounts.util.CompareUtil.lessThan;

@Service
@RequiredArgsConstructor
public class BalanceAdjustmentService {
  private final AccountRepository accountRepository;
  private final HttpStatClient httpStatClient;

  private static final List<String> STATUSES_WITH_ADJUSTABLE_BALANCE = Stream.of(EAccountStatus.ACTIVE)
    .map(EAccountStatus::name)
    .toList();

  @Transactional
  public void addMoneyToAccount(String accountNumber, List<BalanceAdjustmentDto> adjustments) {
    var accountEntity = accountRepository.findAccountByAccountNumber(accountNumber)
      .orElseThrow(() -> new NotFoundException("Account with number=%s not found".formatted(accountNumber)));

    if (!isBalanceAdjustable(accountEntity.getAccountStatus())) {
      throw new BusinessException("Cannot adjust balance, because account=%s is in status=%s".formatted(accountNumber, accountEntity.getAccountStatus().getName()));
    }

    var balances = accountEntity.getAccountBalances();

    for (BalanceAdjustmentDto adjustment : adjustments) {
      AccountBalance balance = findAccountBalanceByCurrency(balances, adjustment.currency())
        .orElseThrow(() -> new BusinessException("Account=%s doesn't support currency '%s'".formatted(accountNumber, adjustment.currency())));

      BigDecimal newAmount = balance.getAmount().add(adjustment.amount());
      if (greaterThan(newAmount, AccountConfig.MAX_BALANCE_AMOUNT)) {
        throw new BusinessException("Account %s has %s %s, so crediting %s %s will exceed maximum of %s"
          .formatted(accountNumber, balance.getAmount(), balance.getCurrency().getCode(), adjustment.amount(), adjustment.currency(), AccountConfig.MAX_BALANCE_AMOUNT));
      }
      balance.setAmount(newAmount);
    }
  }

  @Transactional
  public void debitMoneyFromAccount(String accountNumber, BalanceAdjustmentDto adjustment) {
    httpStatClient.get200Response();

    var accountEntity = accountRepository.findAccountByAccountNumber(accountNumber)
      .orElseThrow(() -> new NotFoundException("Account with number=%s not found".formatted(accountNumber)));

    if (!isBalanceAdjustable(accountEntity.getAccountStatus())) {
      throw new BusinessException("Cannot adjust balance, because account=%s is in status=%s".formatted(accountNumber, accountEntity.getAccountStatus().getName()));
    }

    var balances = accountEntity.getAccountBalances();

    AccountBalance balance = findAccountBalanceByCurrency(balances, adjustment.currency())
      .orElseThrow(() -> new BusinessException("Account=%s doesn't support currency '%s'".formatted(accountNumber, adjustment.currency())));

    BigDecimal newAmount = balance.getAmount().subtract(adjustment.amount());
    if (lessThan(newAmount, AccountConfig.MIN_BALANCE_AMOUNT)) {
      throw new BusinessException("Account %s has %s %s, so debiting %s %s will make amount negative"
        .formatted(accountNumber, balance.getAmount(), balance.getCurrency().getCode(), adjustment.amount(), adjustment.currency()));
    }
    balance.setAmount(newAmount);
  }

  private static Optional<AccountBalance> findAccountBalanceByCurrency(List<AccountBalance> balances, ECurrency currency) {
    return balances.stream()
      .filter(it -> it.getCurrency().getCode().equals(currency.name()))
      .findAny();
  }

  private static boolean isBalanceAdjustable(AccountStatus statusEntity) {
    String status = statusEntity.getName();
    return !status.isBlank() && STATUSES_WITH_ADJUSTABLE_BALANCE.contains(status);
  }
}
