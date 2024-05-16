package com.example.transactionmanagement.controller.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class TransactionDto {
    private BigDecimal amount;
    private String currency;
    private String timestamp;
    private CreditCardDto creditCardDto;
    private RecipientDto recipientDto;
}
