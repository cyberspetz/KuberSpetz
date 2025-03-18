package com.swami.transactionengine.accountmicroservice.controller;

import com.swami.transactionengine.accountmicroservice.Account;
import com.swami.transactionengine.accountmicroservice.service.AccountService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

    public static final String ACCOUNT_TOPIC = "account-topic";
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    KafkaTemplate<String, String> kafkaTemplate;
    AccountService accountService;

    @Autowired
    public AccountController(KafkaTemplate<String, String> kafkaTemplate, AccountService accountService) {
        this.kafkaTemplate = kafkaTemplate;
        this.accountService = accountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(
            @RequestParam @NotNull String accountNumber,
            @RequestParam @NotNull BigDecimal amount) {
        log.info("Received withdraw request for account: {} and amount: {}", accountNumber, amount);
        try {
            String result = accountService.withdraw(accountNumber, amount);
            kafkaTemplate.send(ACCOUNT_TOPIC, String.format("Money withdrawn from account: %s", accountNumber));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("Invalid input for withdrawal request", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process withdrawal request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process withdrawal: " + e.getMessage());
        }
    }

    @GetMapping("/account/info")
    public ResponseEntity<Account> getAccountInfo(@RequestParam String accountNumber) {
        try {
            Account account = accountService.getAccountInfo(accountNumber);
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            log.error("Failed to fetch account info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Account> findAccountById(String id) {
        try {
            Account account = accountService.findAccountById(id);
            if (account == null || account.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            log.error("Failed to find account by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
