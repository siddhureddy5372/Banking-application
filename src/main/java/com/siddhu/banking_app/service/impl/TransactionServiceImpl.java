package com.siddhu.banking_app.service.impl;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.entity.Account;
import com.siddhu.banking_app.entity.Transaction;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.mapper.TransactionMapper;
import com.siddhu.banking_app.repository.AccountRepository;
import com.siddhu.banking_app.repository.TransactionRepository;
import com.siddhu.banking_app.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account does not exist!"));;
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(account);
        return transactions.stream()
                .map(transaction -> TransactionMapper.mapToDTO(transaction, id))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void createTransaction(TransactionCreateDto transaction) {
        Account account_sender = accountRepository.findById(transaction.getSenderId()).orElseThrow(()-> new ResourceNotFoundException("Sender's account does not exist!"));
        Account account_receiver = accountRepository.findById(transaction.getReceiverId()).orElseThrow(()-> new ResourceNotFoundException("Receiver's account does not exist!"));

        if (account_sender.getBalance() < transaction.getAmount()) {
            throw new NotEnoughMoney("Insufficient balance for transaction");
        }

        account_sender.setBalance(account_sender.getBalance()- transaction.getAmount());
        account_receiver.setBalance(account_receiver.getBalance() + transaction.getAmount());

        accountRepository.save(account_sender);
        accountRepository.save(account_receiver);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setSender(account_sender);
        createdTransaction.setReceiver(account_receiver);
        createdTransaction.setAmount(transaction.getAmount());
        createdTransaction.setRecordedOn(LocalDateTime.now());

        transactionRepository.save(createdTransaction);
    }
}
