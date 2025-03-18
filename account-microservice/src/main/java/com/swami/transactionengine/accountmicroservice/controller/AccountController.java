package com.swami.transactionengine.accountmicroservice.controller;

import com.swami.transactionengine.accountmicroservice.Account;
import com.swami.transactionengine.accountmicroservice.service.AccountService;
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
    public ResponseEntity<String> withdrawMoney(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        log.info("Received withdraw request for account: {} and amount: {}", accountNumber, amount);

        try {
            String result = accountService.withdraw(accountNumber, amount);
            kafkaTemplate.send(ACCOUNT_TOPIC, String.format("Money withdrawn from account: %s", accountNumber));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to process withdrawal request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process withdrawal: " + e.getMessage());
        }
    }

    @GetMapping("/account/info")
    public ResponseEntity<Account> getAccountInfo(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountInfo(accountNumber));
    }

}
