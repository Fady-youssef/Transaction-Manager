package com.example.transactionmanagement.service;

import com.example.transactionmanagement.controller.dto.CreditCardDto;
import com.example.transactionmanagement.controller.dto.RecipientDto;
import com.example.transactionmanagement.controller.dto.TransactionDto;
import com.example.transactionmanagement.entity.CreditCard;
import com.example.transactionmanagement.entity.Recipient;
import com.example.transactionmanagement.entity.Transaction;
import com.example.transactionmanagement.mapper.TransactionMapper;
import com.example.transactionmanagement.repository.CreditCardRepository;
import com.example.transactionmanagement.repository.RecipientRepository;
import com.example.transactionmanagement.repository.TransactionRepository;
import com.example.transactionmanagement.util.XmlFileUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final RecipientRepository recipientRepository;
    private final CreditCardRepository creditCardRepository;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, RecipientRepository recipientRepository, CreditCardRepository creditCardRepository) {
        this.transactionRepository = transactionRepository;
        this.recipientRepository = recipientRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public void saveFileXmlToDB(MultipartFile file) throws IOException{
        if(XmlFileUtils.isXmlFile(file)){
            XMLInputFactory factory = XMLInputFactory.newInstance();

            try (InputStream stream = file.getInputStream()) {
                XMLStreamReader reader = factory.createXMLStreamReader(stream);
                List<TransactionDto> transactionDtoList = parseXmlToListTransactionDto(reader);
                if (!transactionDtoList.isEmpty()) {
                    List<Transaction> transactionList = TransactionMapper.mapTransactionDtoListToTransactionList(transactionDtoList);
                    saveTransactions(transactionList);
                }
            } catch (IOException | XMLStreamException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Uploaded file is not an XML file");
        }
    }

    private List<TransactionDto> parseXmlToListTransactionDto(XMLStreamReader reader) throws XMLStreamException {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        TransactionDto transactionDto = null;
        CreditCardDto creditCardDto = null;
        RecipientDto recipientDto = null;
        boolean isCreditCard = false;

        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == XMLEvent.START_ELEMENT || eventType == XMLEvent.END_ELEMENT) {
                String elementName = reader.getName().getLocalPart();
                switch (elementName) {
                    case "Transaction":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            transactionDto = new TransactionDto();
                            transactionDto.setTimestamp(reader.getAttributeValue("http://www.example.com/transactions", "timestamp"));
                            transactionDto.setCurrency(reader.getAttributeValue("http://www.example.com/transactions", "currency"));
                            transactionDto.setAmount(new BigDecimal(reader.getAttributeValue("", "amount")));
                        } else if (eventType == XMLEvent.END_ELEMENT) {
                            transactionDto.setRecipientDto(recipientDto);
                            transactionDto.setCreditCardDto(creditCardDto);
                            transactionDtoList.add(transactionDto);
                            creditCardDto = null;
                            recipientDto = null;
                        }
                        break;
                    case "creditCard":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            isCreditCard = true;
                            creditCardDto = new CreditCardDto();
                        }
                        break;
                    case "number":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            if (creditCardDto != null) creditCardDto.setCardNumber(reader.getElementText());
                        }
                        break;
                    case "name":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            String text = reader.getElementText();
                            if (isCreditCard && creditCardDto != null) {
                                creditCardDto.setCardHolderName(text);
                            } else if (recipientDto != null) {
                                recipientDto.setRecipientName(text);
                            }
                        }
                        break;
                    case "expiryDate":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            if (creditCardDto != null) creditCardDto.setExpiryDate(reader.getElementText());
                        }
                        break;
                    case "cvv":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            if (creditCardDto != null) creditCardDto.setCvv(reader.getElementText());
                        }
                        break;
                    case "email":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            if (recipientDto != null) recipientDto.setRecipientEmail(reader.getElementText());
                        }
                        break;
                    case "phone":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            if (recipientDto != null) recipientDto.setRecipientPhone(reader.getElementText());
                        }
                        break;
                    case "recipient":
                        if (eventType == XMLEvent.START_ELEMENT) {
                            isCreditCard = false;
                            recipientDto = new RecipientDto();
                        }
                        break;
                }
            }
        }
        return transactionDtoList;
    }
    public List<TransactionDto> getTransactionsByCreditCard(String cardNo) {
        List<Transaction> transactionsList = transactionRepository.findByCreditCardNumber(cardNo);
        return TransactionMapper.mapTransactionListToTransactionDtoList(transactionsList);
    }

    @Transactional
    private void saveTransactions(List<Transaction> transactionsList) {
        Set<Recipient> newRecipients = new HashSet<>();
        Set<CreditCard> newCreditCards = new HashSet<>();

        Map<String, Recipient> existingRecipients = recipientRepository.findRecipientMapByNames(
                transactionsList.stream()
                        .map(transaction -> transaction.getRecipient().getRecipientName())
                        .collect(Collectors.toList())
        );
        Map<String, CreditCard> existingCreditCards = creditCardRepository.findCreditCardMapByNumbers(
                transactionsList.stream()
                        .map(transaction -> transaction.getCreditCard().getCardNumber())
                        .collect(Collectors.toList())
        );

        transactionsList.forEach(transaction -> {
            existingRecipients.computeIfAbsent(
                    transaction.getRecipient().getRecipientName(),
                    key -> {
                        newRecipients.add(transaction.getRecipient());
                        return transaction.getRecipient();
                    }
            );

            existingCreditCards.computeIfAbsent(
                    transaction.getCreditCard().getCardNumber(),
                    key -> {
                        newCreditCards.add(transaction.getCreditCard());
                        return transaction.getCreditCard();
                    }
            );
        });

        if (!newRecipients.isEmpty()) {
            recipientRepository.saveAll(newRecipients);
            logger.info("Saved {} recipients", newRecipients.size());
        }

        if (!newCreditCards.isEmpty()) {
            creditCardRepository.saveAll(newCreditCards);
            logger.info("Saved {} credit cards", newCreditCards.size());
        }

        transactionsList.forEach(transaction -> {
            transaction.setRecipient(existingRecipients.get(transaction.getRecipient().getRecipientName()));
            transaction.setCreditCard(existingCreditCards.get(transaction.getCreditCard().getCardNumber()));
        });

        transactionRepository.saveAll(transactionsList);
        transactionsList.clear();
        logger.info("Saved all transactions");
    }


}
