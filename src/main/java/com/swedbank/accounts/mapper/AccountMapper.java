package com.swedbank.accounts.mapper;

import com.swedbank.accounts.dto.AccountBalanceDto;
import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.entity.AccountEntity;
import com.swedbank.accounts.entity.AccountBalanceEntity;

public class AccountMapper {

  public static AccountDto toDto(AccountEntity accountEntity) {
    return new AccountDto(
      accountEntity.getName(),
      accountEntity.getAccountNumber(),
      accountEntity.getAccountType().getName(),
      accountEntity.getAccountStatus().getName(),
      accountEntity.getAccountBalances()
        .stream()
        .map(AccountMapper::toDto)
        .toList()
    );
  }

  public static AccountBalanceDto toDto(AccountBalanceEntity accountBalanceEntity) {
    return new AccountBalanceDto(
      accountBalanceEntity.getAmount(),
      accountBalanceEntity.getCurrency().getCode(),
      accountBalanceEntity.getCurrency().getSign()
    );
  }
}
