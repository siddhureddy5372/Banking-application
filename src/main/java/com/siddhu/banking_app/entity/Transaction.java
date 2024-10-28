package com.siddhu.banking_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "sender", nullable = false)
    private String senderAN;

    @Column(name = "receiver", nullable = false)
    private String receiverAN;

    private double amount;

    private LocalDateTime recordedOn;

    // Pre-set the transaction time when the transaction is created
    @PrePersist
    public void prePersist() {
        recordedOn = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sender='" + senderAN + '\'' +
                ", receiver='" + receiverAN + '\'' +
                ", amount=" + amount +
                ", recordedOn=" + recordedOn +
                '}';
    }
}
