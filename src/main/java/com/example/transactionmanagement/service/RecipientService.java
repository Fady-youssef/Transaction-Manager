package com.example.transactionmanagement.service;

import com.example.transactionmanagement.entity.Transaction;
import com.example.transactionmanagement.repository.RecipientRepository;
import com.example.transactionmanagement.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class RecipientService {
    private final RecipientRepository recipientRepository;
    private final TransactionRepository transactionRepository;

    public RecipientService(RecipientRepository recipientRepository, TransactionRepository transactionRepository) {
        this.recipientRepository = recipientRepository;
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getAmountToReceive(String recipientName) {
        List<Transaction> transactions = transactionRepository.findByRecipientName(recipientName);
        BigDecimal totalAmountToReceive = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            totalAmountToReceive = totalAmountToReceive.add(transaction.getAmount());
        }
        return totalAmountToReceive;
    }

}
