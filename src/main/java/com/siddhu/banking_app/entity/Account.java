package com.siddhu.banking_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="accounts")
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(unique = true, nullable = false, length = 12)
    private String accountNumber;

    @Column(unique = true, nullable = false, length = 12)
        private String ibanNumber;
    private double balance;

    private String accountType;
    private LocalDate creationDate;
    private String status;

    // Pre-set the created time when the account is created
    @PrePersist
    public void prePersist() {
        creationDate = LocalDate.now();
    }
}
