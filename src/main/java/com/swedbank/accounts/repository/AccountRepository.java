package com.swedbank.accounts.repository;

import com.swedbank.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  @Query("""
    SELECT a FROM Account a
    JOIN FETCH a.accountBalances ab
    WHERE a.deletedAt IS NULL AND ab.deletedAt IS NULL
    """)
  List<Account> findAllNotDeletedAccounts();

}
