package com.swedbank.accounts.controller;

import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

  private final AccountService accountService;

  @GetMapping
  public List<AccountDto> getAllAccounts() {
    return accountService.fetchAccounts();
  }
}
