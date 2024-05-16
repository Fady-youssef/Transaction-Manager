package com.example.transactionmanagement.repository;

import com.example.transactionmanagement.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    @Query("SELECT c FROM CreditCard c WHERE c.cardNumber IN :numbers")
    List<CreditCard> findByCardNumbers(@Param("numbers") List<String> numbers);

    default Map<String, CreditCard> findCreditCardMapByNumbers(List<String> numbers) {
        List<CreditCard> creditCards = findByCardNumbers(numbers);
        Map<String, CreditCard> creditCardMap = new HashMap<>();
        for (CreditCard creditCard : creditCards) {
            creditCardMap.put(creditCard.getCardNumber(), creditCard);
        }
        return creditCardMap;
    }
}
