package com.swedbank.accounts.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 128)
  private String name;

  @Column(nullable = false, unique = true, length = 32)
  private String accountNumber;

  @ManyToOne
  @JoinColumn(name = "account_type_id")
  private AccountType accountType;

  @ManyToOne
  @JoinColumn(name = "account_status_id")
  private AccountStatus accountStatus;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<AccountBalance> accountBalances;

  private LocalDateTime deletedAt;
}
