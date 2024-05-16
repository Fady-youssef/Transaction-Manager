package com.example.transactionmanagement.controller;

import com.example.transactionmanagement.controller.dto.TransactionDto;
import com.example.transactionmanagement.service.RecipientService;
import com.example.transactionmanagement.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionService transactionService;
    private final RecipientService recipientService;

    public TransactionController(TransactionService transactionService, RecipientService recipientService) {
        this.transactionService = transactionService;
        this.recipientService = recipientService;
    }
    @PostMapping
    public ResponseEntity<String> receiveAndPersistXML(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No XML file uploaded");
        }

        try {
            transactionService.saveFileXmlToDB(file);
            return ResponseEntity.ok("XML transactions persisted successfully");
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Error processing XML file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing XML file");
        }
    }


    @GetMapping("/recipient/{recipientName}")
    public ResponseEntity<BigDecimal> getRecipientAmount(@PathVariable String recipientName) {
        if (recipientName == null || recipientName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal amount = recipientService.getAmountToReceive(recipientName);
        if (amount != null) {
            return ResponseEntity.ok(amount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/creditcard/{creditCardNumber}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCreditCardNumber(@PathVariable String creditCardNumber) {
        List<TransactionDto> transactions = transactionService.getTransactionsByCreditCard(creditCardNumber);
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }


}


