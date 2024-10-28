package com.siddhu.banking_app.service.impl;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.dto.TransactionRequest;
import com.siddhu.banking_app.entity.Account;
import com.siddhu.banking_app.entity.Transaction;
import com.siddhu.banking_app.exceptions.AccountBlocked;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.mapper.TransactionMapper;
import com.siddhu.banking_app.repository.AccountRepository;
import com.siddhu.banking_app.repository.TransactionRepository;
import com.siddhu.banking_app.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<TransactionDto> getAllTransitionsById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        List<Transaction> transactions = transactionRepository.findTransactionsByAccount(account.getAccountNumber());

        return transactions.stream()
                .map(transaction -> TransactionMapper.mapToDTO(transaction, account.getAccountNumber()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void createTransaction(TransactionCreateDto transaction) {
        Account account_sender = accountRepository.findByAccountNumber(transaction.getSenderAN()).orElseThrow(()-> new ResourceNotFoundException("Sender's account does not exist!"));
        Account account_receiver = accountRepository.findByAccountNumber(transaction.getReceiverAN()).orElseThrow(()-> new ResourceNotFoundException("Receiver's account does not exist!"));
        if (!Objects.equals(account_sender.getStatus(), "active")){
            throw new AccountBlocked("Sender account has been blocked!!");
        }
        if (!Objects.equals(account_receiver.getStatus(), "active")){
            throw new AccountBlocked("Receiver account has been blocked!!");
        }
        if (account_sender.getBalance() < transaction.getAmount()) {
            throw new NotEnoughMoney("Insufficient balance for transaction");
        }
        BigDecimal currentBalance_sender = BigDecimal.valueOf(account_sender.getBalance());
        BigDecimal transferAmount = BigDecimal.valueOf(transaction.getAmount());
        BigDecimal sender_total = currentBalance_sender.subtract(transferAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
        if(sender_total.compareTo(BigDecimal.ZERO)<0){ throw new NotEnoughMoney("Insufficient funds to transfer!");}
        BigDecimal currentBalance_receiver = BigDecimal.valueOf(account_receiver.getBalance());
        BigDecimal receiver_total = currentBalance_receiver.add(transferAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
        account_sender.setBalance(sender_total.doubleValue());
        account_receiver.setBalance(receiver_total.doubleValue());

        accountRepository.save(account_sender);
        accountRepository.save(account_receiver);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setSenderAN(account_sender.getAccountNumber());
        createdTransaction.setReceiverAN(account_receiver.getAccountNumber());
        createdTransaction.setAmount(transaction.getAmount());
        createdTransaction.setRecordedOn(LocalDateTime.now());

        transactionRepository.save(createdTransaction);
    }
    @Override
    public List<TransactionDto> getAllTransitionsByAccountNumber(TransactionRequest accountNumber) {
        Account account =  accountRepository.findByAccountNumber(accountNumber.getAccountNumber())
                .orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));
        System.out.println(account.getAccountHolderName());
        List<Transaction> transactions = transactionRepository.findTransactionsByAccount(account.getAccountNumber());
        return transactions.stream()
                .map(transaction -> TransactionMapper.mapToDTO(transaction, account.getAccountNumber()))
                .collect(Collectors.toList());
    }
}
