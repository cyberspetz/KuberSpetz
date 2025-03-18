package com.swami.transactionengine.accountmicroservice.service;


import com.swami.transactionengine.accountmicroservice.Account;
import com.swami.transactionengine.accountmicroservice.repository.AccountNotFoundException;
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
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    @Transactional
    public String deposit(String accountNumber, BigDecimal amount) {
        log.info("Deposit requested for account: " + accountNumber + " with amount: " + amount);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        return "Deposited: " + amount;

    }

    @Transactional
    public String withdraw(String accountNumber, BigDecimal amount) {
        log.info("Withdrawal requested for account: " + accountNumber + " with amount: " + amount);
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

            if (account.getBalance().compareTo(amount) < 0) {
                return "Insufficient balance";
            }

            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            return "Withdrawn: " + amount;


        } catch (AccountNotFoundException e) {
            log.error("Failed to process withdrawal: {}", e.getMessage());
            return "Account not found";
        } catch (Exception e) {
            log.error("Unexpected error during withdrawal: {}", e.getMessage());
            return "Error processing withdrawal";
        }



    }

    public Account findAccountById(String id) {
        log.info("Finding account by ID: {}", id);

        return accountRepository.findByAccountNumber(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with given Id"));

    }
}

