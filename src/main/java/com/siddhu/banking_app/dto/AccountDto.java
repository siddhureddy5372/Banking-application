package com.siddhu.banking_app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.siddhu.banking_app.Views;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AccountDto {

    @JsonView(Views.Internal.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String accountHolderName;

    @JsonView(Views.Internal.class)
    private String accountNumber;

    @JsonView(Views.Full.class)
    private String ibanNumber;

    @JsonView(Views.Public.class)
    private double balance;

    @JsonView(Views.Public.class)
    private String accountType;

    @JsonView(Views.Internal.class)
    private LocalDate creationDate;

    @JsonView(Views.Internal.class)
    private String status;
}
