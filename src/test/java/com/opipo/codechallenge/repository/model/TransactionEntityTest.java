package com.opipo.codechallenge.repository.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionEntity pojo testing")
public class TransactionEntityTest {

    private TransactionEntity transactionEntity;

    @BeforeEach
    public void init() {
        transactionEntity = new TransactionEntity();
    }

    @Test
    @DisplayName("The getter and the setter of reference work well")
    public void referenceAttributeTest() {
        String reference = Integer.toString(1);
        transactionEntity.setReference(reference);
        assertEquals(reference, transactionEntity.getReference());
    }

    @Test
    @DisplayName("The getter and the setter of accountIban work well")
    public void accountIbanAttributeTest() {
        String accountIban = Integer.toString(2);
        transactionEntity.setAccountIban(accountIban);
        assertEquals(accountIban, transactionEntity.getAccountIban());
    }

    @Test
    @DisplayName("The getter and the setter of date work well")
    public void dateAttributeTest() {
        OffsetDateTime date = this.buildOffsetDateTime();
        transactionEntity.setDate(date);
        assertEquals(date, transactionEntity.getDate());
    }

    private OffsetDateTime buildOffsetDateTime() {
        return OffsetDateTime.now();
    }

    @Test
    @DisplayName("The getter and the setter of amount work well")
    public void amountAttributeTest() {
        BigDecimal amount = BigDecimal.valueOf(1.0D);
        transactionEntity.setAmount(amount);
        assertEquals(amount, transactionEntity.getAmount());
    }

    @Test
    @DisplayName("The getter and the setter of fee work well")
    public void feeAttributeTest() {
        BigDecimal fee = BigDecimal.valueOf(2.0D);
        transactionEntity.setFee(fee);
        assertEquals(fee, transactionEntity.getFee());
    }

    @Test
    @DisplayName("The getter and the setter of description work well")
    public void descriptionAttributeTest() {
        String description = Integer.toString(3);
        transactionEntity.setDescription(description);
        assertEquals(description, transactionEntity.getDescription());
    }

    @Test
    @DisplayName("The same object are equals")
    public void givenSameObjReturnThatTheyAreEquals() {
        TransactionEntity o1 = new TransactionEntity();
        TransactionEntity o2 = new TransactionEntity();
        assertEquals(o1, o2);
    }

    @Test
    @DisplayName("The object and a String aren't equals")
    public void givenObjectFromOtherClassReturnThatTheyArentEquals() {
        TransactionEntity o1 = new TransactionEntity();
        assertNotEquals(o1, new String());
    }

    @Test
    @DisplayName("The same object's hashcode are the same")
    public void givenSameObjReturnSameHashCode() {
        TransactionEntity o1 = new TransactionEntity();
        TransactionEntity o2 = new TransactionEntity();
        assertEquals(o1.hashCode(), o2.hashCode());
    }
}