package com.siddhu.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    private String otherPartyName;
    private double amount;
    private String role; // either "sender" or "receiver"
    private String recordedOn;


}
