package com.example.transactionmanagement;


import com.example.transactionmanagement.controller.TransactionController;
import com.example.transactionmanagement.controller.dto.TransactionDto;
import com.example.transactionmanagement.service.RecipientService;
import com.example.transactionmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private RecipientService recipientService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceiveAndPersistXML_Success() throws IOException {
        // Mock behavior
        doNothing().when(transactionService).saveFileXmlToDB(any());

        // Create a mock XML file
        MockMultipartFile file = new MockMultipartFile("file", "transactions.xml", "application/xml", "<dummy>data</dummy>".getBytes());

        // Call the method under test
        ResponseEntity<String> response = transactionController.receiveAndPersistXML(file);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("XML transactions persisted successfully", response.getBody());
    }

    @Test
    public void testReceiveAndPersistXML_EmptyFile() {
        // Create an empty mock XML file
        MockMultipartFile file = new MockMultipartFile("file", "transactions.xml", "application/xml", new byte[0]);

        // Call the method under test
        ResponseEntity<String> response = transactionController.receiveAndPersistXML(file);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No XML file uploaded", response.getBody());
    }

    @Test
    public void testReceiveAndPersistXML_Exception() throws IOException {
        // Mock behavior to throw IOException
        doThrow(IOException.class).when(transactionService).saveFileXmlToDB(any());

        // Create a mock XML file
        MockMultipartFile file = new MockMultipartFile("file", "transactions.xml", "application/xml", "<dummy>data</dummy>".getBytes());

        // Call the method under test
        ResponseEntity<String> response = transactionController.receiveAndPersistXML(file);

        // Verify the response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error processing XML file", response.getBody());
    }

    @Test
    public void testGetRecipientAmount_Success() {
        // Mock behavior
        BigDecimal expectedAmount = new BigDecimal("100.00");
        when(recipientService.getAmountToReceive(anyString())).thenReturn(expectedAmount);

        // Call the method under test
        ResponseEntity<BigDecimal> response = transactionController.getRecipientAmount("Alice");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedAmount, response.getBody());
    }

    @Test
    public void testGetRecipientAmount_NullRecipientName() {
        // Call the method under test with null recipient name
        ResponseEntity<BigDecimal> response = transactionController.getRecipientAmount(null);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetTransactionsByCreditCardNumber_Success() {
        // Mock behavior
        List<TransactionDto> expectedTransactions = Collections.singletonList(new TransactionDto());
        when(transactionService.getTransactionsByCreditCard(anyString())).thenReturn(expectedTransactions);

        // Call the method under test
        ResponseEntity<List<TransactionDto>> response = transactionController.getTransactionsByCreditCardNumber("1234567890123456");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedTransactions, response.getBody());
    }

    @Test
    public void testGetTransactionsByCreditCardNumber_NoTransactionsFound() {
        // Mock behavior
        when(transactionService.getTransactionsByCreditCard(anyString())).thenReturn(Collections.emptyList());

        // Call the method under test
        ResponseEntity<List<TransactionDto>> response = transactionController.getTransactionsByCreditCardNumber("1234567890123456");

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
