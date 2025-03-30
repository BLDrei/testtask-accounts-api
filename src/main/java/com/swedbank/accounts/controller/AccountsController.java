package com.swedbank.accounts.controller;

import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.dto.balance_adjustment.BalanceAdjustmentDto;
import com.swedbank.accounts.service.AccountService;
import com.swedbank.accounts.service.BalanceAdjustmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Validated
public class AccountsController {

  private final AccountService accountService;
  private final BalanceAdjustmentService balanceAdjustmentService;

  @GetMapping
  public List<AccountDto> getAllAccounts() {
    return accountService.fetchAccounts();
  }

  @GetMapping("{accountNumber}")
  public AccountDto getAllAccounts(@PathVariable String accountNumber) {
    return accountService.fetchAccountByAccountNumber(accountNumber);
  }

  @PatchMapping("{accountNumber}/credit")
  @Operation(summary = "Add money to account")
  public void addMoneyToAccount(@PathVariable String accountNumber,
                                @RequestBody @NotEmpty List<@Valid BalanceAdjustmentDto> balances) {
    balanceAdjustmentService.addMoneyToAccount(accountNumber, balances);
  }

  @PatchMapping("{accountNumber}/debit")
  @Operation(summary = "Debit money from account")
  public void debitMoneyFromAccount(@PathVariable String accountNumber,
                                    @RequestBody @Valid BalanceAdjustmentDto balance) {
    balanceAdjustmentService.debitMoneyFromAccount(accountNumber, balance);
  }
}
