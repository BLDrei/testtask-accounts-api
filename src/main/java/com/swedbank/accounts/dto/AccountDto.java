package com.swedbank.accounts.dto;

import java.util.List;

public record AccountDto(
  String name,
  String accountNumber,
  String type,
  String status,
  List<AccountBalanceDto> balances
) {}
