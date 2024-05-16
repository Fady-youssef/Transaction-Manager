package com.example.transactionmanagement.repository;

import com.example.transactionmanagement.entity.Recipient;
import com.example.transactionmanagement.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.recipient.recipientName = :recipientName")
    List<Transaction> findByRecipientName(@Param("recipientName") String recipientName);
    @Query("SELECT t FROM Transaction t WHERE t.creditCard.cardNumber = :cardNumber")
    List<Transaction> findByCreditCardNumber(String cardNumber);

}
