package com.siddhu.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionCreateDto {
    private String senderAN;
    private String receiverAN;
    private double amount;
}

