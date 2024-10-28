package com.siddhu.banking_app.service;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.dto.TransactionRequest;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransitionsById(Long id);
    void createTransaction(TransactionCreateDto transaction);
    List<TransactionDto> getAllTransitionsByAccountNumber(TransactionRequest accountNumber);
}
