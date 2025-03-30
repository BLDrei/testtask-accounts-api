package com.swedbank.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AccountBalanceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, precision = 11, scale = 2)
  private BigDecimal amount;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private AccountEntity account;

  @ManyToOne
  @JoinColumn(name = "currency_id", nullable = false)
  private CurrencyEntity currency;

  private LocalDateTime deletedAt;
}
