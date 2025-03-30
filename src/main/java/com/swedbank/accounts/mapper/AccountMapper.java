package com.swedbank.accounts.mapper;

import com.swedbank.accounts.dto.AccountBalanceDto;
import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.entity.Account;
import com.swedbank.accounts.entity.AccountBalance;

public class AccountMapper {

  public static AccountDto toDto(Account account) {
    return new AccountDto(
      account.getName(),
      account.getAccountNumber(),
      account.getAccountType().getName(),
      account.getAccountStatus().getName(),
      account.getAccountBalances()
        .stream()
        .map(AccountMapper::toDto)
        .toList()
    );
  }

  public static AccountBalanceDto toDto(AccountBalance accountBalance) {
    return new AccountBalanceDto(
      accountBalance.getAmount(),
      accountBalance.getCurrency().getCode(),
      accountBalance.getCurrency().getSign()
    );
  }
}
