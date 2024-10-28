package com.siddhu.banking_app.exceptions;

public class AccountBlocked extends RuntimeException{
    public AccountBlocked(String message) {
        super(message);

    }
}
