package com.example.transactionmanagement.repository;

import com.example.transactionmanagement.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RecipientRepository extends JpaRepository<Recipient,Long> {

    Recipient findByRecipientName(String name);

    @Query("SELECT r FROM Recipient r WHERE r.recipientName IN :names")
    List<Recipient> findByRecipientNames(@Param("names") List<String> names);

    default Map<String, Recipient> findRecipientMapByNames(List<String> names) {
        List<Recipient> recipients = findByRecipientNames(names);
        Map<String, Recipient> recipientMap = new HashMap<>();
        for (Recipient recipient : recipients) {
            recipientMap.put(recipient.getRecipientName(), recipient);
        }
        return recipientMap;
    }
}
