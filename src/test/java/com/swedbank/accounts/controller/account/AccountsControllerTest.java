package com.swedbank.accounts.controller.account;

import com.swedbank.accounts.dto.AccountBalanceDto;
import com.swedbank.accounts.dto.AccountDto;
import com.swedbank.accounts.exception.NotFoundException;
import com.swedbank.accounts.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.swedbank.accounts.util.BigDecimalUtil.bd;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AccountService accountService;

  private AccountDto accountDto;

  @BeforeEach
  void setup() {
    accountDto = new AccountDto("Test Account", "1234567890", "DEBIT", "ACTIVE", List.of(new AccountBalanceDto(bd("1000.00"), "USD", "$")));
  }

  @Test
  public void testGetAllAccounts() throws Exception {
    when(accountService.fetchAccounts()).thenReturn(Collections.singletonList(accountDto));

    mockMvc.perform(get("/accounts"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].accountNumber").value(accountDto.accountNumber()))
      .andExpect(jsonPath("$[0].balances.[0].amount").value(1000.0))
      .andExpect(jsonPath("$[0].balances.[0].currencyCode").value("USD"));
  }

  @Test
  public void testGetAccountByAccountNumber() throws Exception {
    when(accountService.fetchAccountByAccountNumber("1234567890")).thenReturn(accountDto);

    mockMvc.perform(get("/accounts/1234567890"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accountNumber").value(accountDto.accountNumber()))
      .andExpect(jsonPath("$.name").value(accountDto.name()))
      .andExpect(jsonPath("$.balances.[0].amount").value(1000.0))
      .andExpect(jsonPath("$.balances.[0].currencyCode").value("USD"));
  }

  @Test
  public void testGetAccountByAccountNumber_notFound() throws Exception {
    when(accountService.fetchAccountByAccountNumber("1234567890")).thenThrow(new NotFoundException(""));

    mockMvc.perform(get("/accounts/1234567890"))
      .andExpect(status().isNotFound());
  }
}
