package com.siddhu.banking_app.exceptions;

public class NotEnoughMoney extends RuntimeException{
    public NotEnoughMoney(String message) {
        super(message);

    }
}
