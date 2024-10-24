package com.siddhu.banking_app.service;

import com.siddhu.banking_app.dto.AccountDto;

import java.util.List;


public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    AccountDto deposit(Long id, double amount);
    AccountDto withdraw(Long id, double amount);
    List<AccountDto> getAllAccounts();
    Double getBalance(Long id);

    void deleteAccountById(Long id);

}
