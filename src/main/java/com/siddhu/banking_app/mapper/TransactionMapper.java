package com.siddhu.banking_app.mapper;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.entity.Transaction;

public class TransactionMapper {

    public static TransactionCreateDto mapToTotalDto(Transaction transaction){
        TransactionCreateDto transactionsDto = new TransactionCreateDto(
                transaction.getSender().getId(),
                transaction.getReceiver().getId(),
                transaction.getAmount(),
                transaction.getRecordedOn()
        );
        return transactionsDto;
    }
    public static TransactionDto mapToDTO(Transaction transaction, Long currentUserId) {
        if (transaction.getSender().getId().equals(currentUserId)) {
            // If the current user is the sender, return the receiver's information
            return new TransactionDto(
                    transaction.getReceiver().getAccountHolderName(),
                    transaction.getAmount(),
                    "sender",
                    transaction.getRecordedOn().toString()
            );
        } else if (transaction.getReceiver().getId().equals(currentUserId)) {
            // If the current user is the receiver, return the sender's information
            return new TransactionDto(
                    transaction.getSender().getAccountHolderName(),
                    transaction.getAmount(),
                    "receiver",
                    transaction.getRecordedOn().toString()
            );
        } else {
            throw new IllegalArgumentException("User is not part of this transaction");
        }
    }
}
