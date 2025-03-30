package com.swedbank.accounts.service;

import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.exception.NotFoundException;
import com.swedbank.accounts.mapper.AccountMapper;
import com.swedbank.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  public List<AccountDto> fetchAccounts() {
    return accountRepository.findAllAccounts()
      .stream()
      .map(AccountMapper::toDto)
      .toList();
  }

  public AccountDto fetchAccountByAccountNumber(String accountNumber) {
    var accountEntity = accountRepository.findAccountByAccountNumber(accountNumber)
      .orElseThrow(() -> new NotFoundException("Cannot find account by accountNumber=%s".formatted(accountNumber)));

    return AccountMapper.toDto(accountEntity);
  }
}
