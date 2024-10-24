package com.siddhu.banking_app.service.impl;

import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.entity.Account;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.mapper.AccountMapper;
import com.siddhu.banking_app.repository.AccountRepository;
import com.siddhu.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
    public AccountDto deposit(Long id, double amount) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        BigDecimal currentBalance = BigDecimal.valueOf(account.getBalance());
        BigDecimal depositBalance = BigDecimal.valueOf(amount);
        BigDecimal total = currentBalance.add(depositBalance).setScale(2,BigDecimal.ROUND_HALF_UP);
        account.setBalance(total.doubleValue());
        Account saved_account = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(saved_account);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account =  accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        BigDecimal currentBalance = BigDecimal.valueOf(account.getBalance());
        BigDecimal withdrawBalance = BigDecimal.valueOf(amount);
        BigDecimal total = currentBalance.subtract(withdrawBalance).setScale(2,BigDecimal.ROUND_HALF_UP);
        if(total.compareTo(BigDecimal.ZERO) >= 0){
            account.setBalance(total.doubleValue());
            Account saved_account = accountRepository.save(account);
            return AccountMapper.mapToAccountDto(saved_account);
        }else {
            throw new NotEnoughMoney("Funds not sufficient!");
        }
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
}
