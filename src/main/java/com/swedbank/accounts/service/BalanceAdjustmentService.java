package com.swedbank.accounts.service;

import com.swedbank.accounts.config.AccountConfig;
import com.swedbank.accounts.dto.AccountStatus;
import com.swedbank.accounts.dto.Currency;
import com.swedbank.accounts.dto.balance_change.BalanceAdjustmentDto;
import com.swedbank.accounts.entity.AccountBalanceEntity;
import com.swedbank.accounts.entity.AccountStatusEntity;
import com.swedbank.accounts.exception.BusinessException;
import com.swedbank.accounts.exception.NotFoundException;
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

  private static final List<String> STATUSES_WITH_ADJUSTABLE_BALANCE = Stream.of(AccountStatus.ACTIVE)
    .map(AccountStatus::name)
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
      AccountBalanceEntity balance = findAccountBalanceByCurrency(balances, adjustment.currency())
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
    var accountEntity = accountRepository.findAccountByAccountNumber(accountNumber)
      .orElseThrow(() -> new NotFoundException("Account with number=%s not found".formatted(accountNumber)));

    if (!isBalanceAdjustable(accountEntity.getAccountStatus())) {
      throw new BusinessException("Cannot adjust balance, because account=%s is in status=%s".formatted(accountNumber, accountEntity.getAccountStatus().getName()));
    }

    var balances = accountEntity.getAccountBalances();

    AccountBalanceEntity balance = findAccountBalanceByCurrency(balances, adjustment.currency())
      .orElseThrow(() -> new BusinessException("Account=%s doesn't support currency '%s'".formatted(accountNumber, adjustment.currency())));

    BigDecimal newAmount = balance.getAmount().subtract(adjustment.amount());
    if (lessThan(newAmount, AccountConfig.MIN_BALANCE_AMOUNT)) {
      throw new BusinessException("Account %s has %s %s, so debiting %s %s will make amount negative"
        .formatted(accountNumber, balance.getAmount(), balance.getCurrency().getCode(), adjustment.amount(), adjustment.currency()));
    }
    balance.setAmount(newAmount);
  }

  private static Optional<AccountBalanceEntity> findAccountBalanceByCurrency(List<AccountBalanceEntity> balances, Currency currency) {
    return balances.stream()
      .filter(it -> it.getCurrency().getCode().equals(currency.name()))
      .findAny();
  }

  private static boolean isBalanceAdjustable(AccountStatusEntity statusEntity) {
    String status = statusEntity.getName();
    return !status.isBlank() && STATUSES_WITH_ADJUSTABLE_BALANCE.contains(status);
  }
}
