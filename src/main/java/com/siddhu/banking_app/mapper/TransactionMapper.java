package com.siddhu.banking_app.mapper;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.entity.Transaction;

public class TransactionMapper {

    public static TransactionCreateDto mapToTotalDto(Transaction transaction){
        TransactionCreateDto transactionsDto = new TransactionCreateDto(
                transaction.getSenderAN(),
                transaction.getReceiverAN(),
                transaction.getAmount()
        );
        return transactionsDto;
    }
    public static TransactionDto mapToDTO(Transaction transaction, String currentUserAN) {
        if (transaction.getSenderAN().equals(currentUserAN)) {
            // If the current user is the sender, return the receiver's information
            return new TransactionDto(
                    transaction.getReceiverAN(),
                    transaction.getAmount(),
                    "sender",
                    transaction.getRecordedOn().toString()
            );
        } else if (transaction.getReceiverAN().equals(currentUserAN)) {
            // If the current user is the receiver, return the sender's information
            return new TransactionDto(
                    transaction.getSenderAN(),
                    transaction.getAmount(),
                    "receiver",
                    transaction.getRecordedOn().toString()
            );
        } else {
            throw new IllegalArgumentException("User is not part of this transaction");
        }
    }
}
