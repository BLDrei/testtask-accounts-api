package com.swedbank.accounts.service;

import com.swedbank.accounts.dto.AccountDto;
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
    return accountRepository.findAllNotDeletedAccounts()
      .stream()
      .map(AccountMapper::toDto)
      .toList();
  }
}
