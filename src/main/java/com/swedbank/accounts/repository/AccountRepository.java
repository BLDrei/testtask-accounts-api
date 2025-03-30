package com.swedbank.accounts.repository;

import com.swedbank.accounts.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

  @Query("""
    SELECT a FROM Account a
    JOIN FETCH a.accountBalances ab
    WHERE a.deletedAt IS NULL AND ab.deletedAt IS NULL
    """)
  List<AccountEntity> findAllAccounts();

  @Query("""
    SELECT a FROM Account a
    JOIN FETCH a.accountBalances ab
    WHERE a.deletedAt IS NULL AND ab.deletedAt IS NULL
    AND a.accountNumber = :accountNumber
    """)
  Optional<AccountEntity> findAccountByAccountNumber(String accountNumber);

}
