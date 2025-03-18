package com.swami.transactionengine.accountmicroservice.service;


import com.swami.transactionengine.accountmicroservice.Account;
import com.swami.transactionengine.accountmicroservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service

public class AccountService {
    private final AccountRepository accountRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountInfo(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public String deposit(String accountNumber, BigDecimal amount) {
        log.info("Deposit requested for account: " + accountNumber + " with amount: " + amount);
        Account acc = accountRepository.findByAccountNumber(accountNumber);
        if (acc != null) {
            acc.setBalance(acc.getBalance().add(amount));
            accountRepository.save(acc);
            return "Deposited: " + amount;
        } else {
            return "Account not found";
        }
    }

    @Transactional
    public String withdraw(String accountNumber, BigDecimal amount) {
        log.info("Withdrawal requested for account: " + accountNumber + " with amount: " + amount);
        Account acc = accountRepository.findByAccountNumber(accountNumber);
        if (acc != null && acc.getBalance().compareTo(amount) >= 0) {
            acc.setBalance(acc.getBalance().subtract(amount));
            accountRepository.save(acc);
            return "Withdrawn: " + amount;
        } else {
            return "Insufficient balance or account not found";
        }
    }
}

