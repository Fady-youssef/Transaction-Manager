package com.example.transactionmanagement.mapper;

import com.example.transactionmanagement.controller.dto.CreditCardDto;
import com.example.transactionmanagement.controller.dto.RecipientDto;
import com.example.transactionmanagement.controller.dto.TransactionDto;
import com.example.transactionmanagement.entity.CreditCard;
import com.example.transactionmanagement.entity.Recipient;
import com.example.transactionmanagement.entity.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.transactionmanagement.util.DateUtils.parseTimestamp;

public class TransactionMapper {
    public static List<Transaction> mapTransactionDtoListToTransactionList(List<TransactionDto> transactionDtoList) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionDtoList.forEach(transactionDto -> {
            Recipient recipient = mapToRecipient(transactionDto.getRecipientDto());
            CreditCard creditCard = mapToCreditCard(transactionDto.getCreditCardDto());
            LocalDateTime timestamp = parseTimestamp(transactionDto.getTimestamp());
            Transaction transaction = mapToTransaction(transactionDto, recipient, creditCard, timestamp);
            transactionList.add(transaction);
        });
        return transactionList;
    }

    public static Recipient mapToRecipient(RecipientDto recipientDto) {
        return Recipient.builder()
                .recipientName(recipientDto.getRecipientName())
                .recipientPhone(recipientDto.getRecipientPhone())
                .recipientEmail(recipientDto.getRecipientEmail())
                .build();
    }
    public static RecipientDto mapToRecipientDto(Recipient recipient) {
        return RecipientDto.builder()
                .recipientName(recipient.getRecipientName())
                .recipientPhone(recipient.getRecipientPhone())
                .recipientEmail(recipient.getRecipientEmail())
                .build();
    }

    public static CreditCard mapToCreditCard(CreditCardDto creditCardDto) {
        return CreditCard.builder()
                .cardHolderName(creditCardDto.getCardHolderName())
                .expiryDate(creditCardDto.getExpiryDate())
                .cardNumber(creditCardDto.getCardNumber())
                .cvv(creditCardDto.getCvv())
                .build();
    }

    public static Transaction mapToTransaction(TransactionDto transactionDto, Recipient recipient, CreditCard creditCard, LocalDateTime timestamp) {
        return Transaction.builder()
                .amount(transactionDto.getAmount())
                .currency(transactionDto.getCurrency())
                .timestamp(timestamp)
                .recipient(recipient)
                .creditCard(creditCard)
                .build();
    }
    public static List<TransactionDto> mapTransactionListToTransactionDtoList(List<Transaction> transactionList) {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionList.forEach(transaction -> {
            RecipientDto recipientDto = mapToRecipientDto(transaction.getRecipient());
            CreditCardDto creditCardDto = mapToCreditCardDto(transaction.getCreditCard());
            LocalDateTime timestamp = transaction.getTimestamp();
            TransactionDto transactionDto = mapToTransactionDto(transaction, recipientDto, creditCardDto, timestamp);
            transactionDtoList.add(transactionDto);
        });
        return transactionDtoList;
    }
    public static TransactionDto mapToTransactionDto(Transaction transaction, RecipientDto recipientDto, CreditCardDto creditCardDto, LocalDateTime timestamp) {
        return TransactionDto.builder()
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .timestamp(timestamp.toString())
                .recipientDto(recipientDto)
                .creditCardDto(creditCardDto)
                .build();
    }
    public static CreditCardDto mapToCreditCardDto(CreditCard creditCard) {
        return CreditCardDto.builder()
                .cardHolderName(creditCard.getCardHolderName())
                .expiryDate(creditCard.getExpiryDate())
                .cardNumber(creditCard.getCardNumber())
                .cvv(creditCard.getCvv())
                .build();
    }


}
