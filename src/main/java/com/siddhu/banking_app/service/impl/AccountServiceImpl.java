package com.siddhu.banking_app.service.impl;

import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.entity.Account;
import com.siddhu.banking_app.exceptions.AccountBlocked;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.mapper.AccountMapper;
import com.siddhu.banking_app.repository.AccountRepository;
import com.siddhu.banking_app.service.AccountService;
import com.siddhu.banking_app.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private TransactionService transactionService;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account saved_account = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(saved_account);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        return AccountMapper.mapToAccountDto(account);

    }

    @Override
    public AccountDto getAccountByAccountNumber(String accountNumber) {
        Account account =  accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Transactional
    @Override
    public TransactionCreateDto deposit(Long id, double amount) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        if (!Objects.equals(account.getStatus(), "active")){
            throw new AccountBlocked("Account has been blocked!!");
        }
        if(amount <= 0){throw new IllegalArgumentException("Can't process negative amount!!");}
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto("Bank_ATM_CMA", account.getAccountNumber(),amount);
        transactionService.createTransaction(transactionCreateDto);
        return transactionCreateDto;
    }

    @Transactional
    @Override
    public TransactionCreateDto withdraw(Long id, double amount) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        if (!Objects.equals(account.getStatus(), "active")){
            throw new AccountBlocked("Account has been blocked!!");
        }
        if(amount <= 0){throw new IllegalArgumentException("Can't process negative amount!!");}


            TransactionCreateDto transactionCreateDto = new TransactionCreateDto(account.getAccountNumber(),"Bank_ATM_CMA",amount);
            transactionService.createTransaction(transactionCreateDto);
            return transactionCreateDto;

    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public Double getBalance(Long id) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        return account.getBalance();
    }


    @Override
    public void deleteAccountById(Long id) {
        accountRepository.findById(id).ifPresentOrElse(accountRepository::delete, () -> {
            throw new ResourceNotFoundException("Account not found!");
        });
    }

    @Override
    public void deactivateAccountByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        account.setStatus("blocked");
        accountRepository.save(account);
    }

    @Override
    public void reactivateAccountByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        account.setStatus("active");
        accountRepository.save(account);
    }
}
