package com.siddhu.banking_app.service;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransitionsById(Long id);
    void createTransaction(TransactionCreateDto transaction);
}
