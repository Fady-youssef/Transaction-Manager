package com.example.transactionmanagement.controller.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CreditCardDto {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

}
