package com.siddhu.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionCreateDto {
    private Long senderId;
    private Long receiverId;
    private double amount;
    private LocalDateTime recordedOn;
}

