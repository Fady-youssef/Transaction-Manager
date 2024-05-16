package com.example.transactionmanagement;

import com.example.transactionmanagement.entity.Transaction;
import com.example.transactionmanagement.repository.RecipientRepository;
import com.example.transactionmanagement.repository.TransactionRepository;
import com.example.transactionmanagement.service.RecipientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RecipientServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RecipientService recipientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAmountToReceive() {
        // Mock data
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(new BigDecimal("100"));
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(new BigDecimal("200"));
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        // Mock repository behavior
        when(transactionRepository.findByRecipientName("Recipient")).thenReturn(transactions);

        // Call the service method
        BigDecimal totalAmount = recipientService.getAmountToReceive("Recipient");

        // Verify the result
        assertEquals(new BigDecimal("300"), totalAmount);
    }

    @Test
    public void testGetAmountToReceiveWithNoTransactions() {
        // Mock repository behavior
        when(transactionRepository.findByRecipientName("Recipient")).thenReturn(Arrays.asList());

        // Call the service method
        BigDecimal totalAmount = recipientService.getAmountToReceive("Recipient");

        // Verify the result
        assertEquals(BigDecimal.ZERO, totalAmount);
    }
}
