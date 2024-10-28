package com.siddhu.banking_app.repository;

import com.siddhu.banking_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.senderAN = :account OR t.receiverAN = :account ORDER BY t.recordedOn DESC")
    List<Transaction> findTransactionsByAccount(@Param("account") String account);

    List<Transaction> findBySenderAN(String accountNumber);
    List<Transaction> findByReceiverAN(String accountNumber);
}
