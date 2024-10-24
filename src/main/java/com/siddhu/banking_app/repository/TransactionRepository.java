package com.siddhu.banking_app.repository;

import com.siddhu.banking_app.entity.Account;
import com.siddhu.banking_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sender = :account OR t.receiver = :account ORDER BY t.recordedOn DESC")
    List<Transaction> findTransactionsByAccountId(@Param("account") Account account);
}
