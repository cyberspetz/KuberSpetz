package com.swami.transactionengine.accountmicroservice.repository;

import com.swami.transactionengine.accountmicroservice.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}
