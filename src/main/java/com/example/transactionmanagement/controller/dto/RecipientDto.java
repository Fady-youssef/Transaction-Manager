package com.example.transactionmanagement.controller.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RecipientDto {
    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;

}
