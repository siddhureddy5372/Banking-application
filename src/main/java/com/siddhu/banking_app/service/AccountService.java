package com.siddhu.banking_app.service;

import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.dto.TransactionCreateDto;

import java.util.List;


public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    AccountDto getAccountByAccountNumber(String accountNumber);
    TransactionCreateDto deposit(Long id, double amount);
    TransactionCreateDto withdraw(Long id, double amount);
    List<AccountDto> getAllAccounts();
    Double getBalance(Long id);

    void deleteAccountById(Long id);
    void deactivateAccountByAccount(String accountNumber);
    void reactivateAccountByAccount(String accountNumber);

}
