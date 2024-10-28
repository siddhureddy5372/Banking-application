package com.siddhu.banking_app.mapper;

import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.entity.Account;

public class AccountMapper {
    public static Account mapToAccount(AccountDto accountDto){
        Account account = new Account(
                accountDto.getId(),
                accountDto.getAccountHolderName(),
                accountDto.getAccountNumber(),
                accountDto.getIbanNumber(),
                accountDto.getBalance(),
                accountDto.getAccountType(),
                accountDto.getCreationDate(),
                accountDto.getStatus()
        );
        return account;
    }

    public static AccountDto mapToAccountDto(Account account){
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getAccountHolderName(),
                account.getAccountNumber(),
                account.getIbanNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreationDate(),
                account.getStatus()
        );
        return accountDto;
    }
}
