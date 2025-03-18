package com.swami.transactionengine.accountmicroservice.repository;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

}
