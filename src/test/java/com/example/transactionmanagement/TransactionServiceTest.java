package com.example.transactionmanagement;


import com.example.transactionmanagement.entity.CreditCard;
import com.example.transactionmanagement.entity.Recipient;
import com.example.transactionmanagement.entity.Transaction;
import com.example.transactionmanagement.repository.CreditCardRepository;
import com.example.transactionmanagement.repository.RecipientRepository;
import com.example.transactionmanagement.repository.TransactionRepository;
import com.example.transactionmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveFileXmlToDB() throws IOException {
        // Mock behavior
        when(transactionRepository.saveAll(any())).thenReturn(null);
        when(recipientRepository.saveAll(any())).thenReturn(null);
        when(creditCardRepository.saveAll(any())).thenReturn(null);

        // Create a mock XML file
        String xmlData = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<transactions xmlns:x=\"http://www.example.com/transactions\">\n" +
                "    <Transaction amount=\"434.8086890366306\" x:currency=\"JPY\" x:timestamp=\"2024-05-07T08:18:11.946710406Z\">\n" +
                "        <creditCard>\n" +
                "            <number>1234-5678-1234-1059</number>\n" +
                "            <name>Bob</name>\n" +
                "            <expiryDate>12/24</expiryDate>\n" +
                "            <cvv/>\n" +
                "        </creditCard>\n" +
                "        <recipient>\n" +
                "            <name>Alice</name>\n" +
                "            <email/>\n" +
                "            <phone>+1234567890</phone>\n" +
                "        </recipient>\n" +
                "    </Transaction>\n" +
                "</transactions>";

        MockMultipartFile file = new MockMultipartFile("file", "transactions.xml", "application/xml", xmlData.getBytes());

        // Call the method under test
        transactionService.saveFileXmlToDB(file);

        // Verify that the save methods were called
        verify(transactionRepository, times(1)).saveAll(any());
        verify(recipientRepository, times(1)).saveAll(any());
        verify(creditCardRepository, times(1)).saveAll(any());
    }}
