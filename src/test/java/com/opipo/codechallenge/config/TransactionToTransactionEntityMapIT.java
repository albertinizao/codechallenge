package com.opipo.codechallenge.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.opipo.codechallenge.CodechallengeApplication;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.web.api.model.Transaction;

@SpringBootTest(classes = CodechallengeApplication.class)
@DisplayName("TransactionToTransactionEntityMap Integration test")
public class TransactionToTransactionEntityMapIT {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("Given Transaction then transform it to TransactionEntity")
    void givenTransactionThenTransformToTransactionEntity() {
        String accountIban = "myaccount";
        BigDecimal amount = BigDecimal.valueOf(48d);
        OffsetDateTime date = OffsetDateTime.now();
        String description = "my description";
        BigDecimal fee = BigDecimal.valueOf(3.5d);
        String reference = "referencee";
        Transaction transaction = buildTransaction(accountIban, amount, date, description, fee, reference);

        TransactionEntity actual = modelMapper.map(transaction, TransactionEntity.class);

        checkTransactionEntityIsCorrect(actual, accountIban, amount, date, description, fee, reference);
    }

    @Test
    @DisplayName("Given Transaction without reference then transform it to TransactionEntity")
    void givenTransactionWithoutReferenceThenTransformToTransactionEntity() {
        String accountIban = "myaccount";
        BigDecimal amount = BigDecimal.valueOf(48d);
        OffsetDateTime date = OffsetDateTime.now();
        String description = "my description";
        BigDecimal fee = BigDecimal.valueOf(3.5d);
        String reference = null;
        Transaction transaction = buildTransaction(accountIban, amount, date, description, fee, reference);

        TransactionEntity actual = modelMapper.map(transaction, TransactionEntity.class);

        checkTransactionEntityIsCorrect(actual, accountIban, amount, date, description, fee, reference);
    }

    private void checkTransactionEntityIsCorrect(TransactionEntity transaction, String accountIban, BigDecimal amount,
            OffsetDateTime date, String description, BigDecimal fee, String reference) {
        assertEquals(accountIban, transaction.getAccountIban());
        assertEquals(amount, transaction.getAmount());
        assertEquals(date, transaction.getDate());
        assertEquals(description, transaction.getDescription());
        assertEquals(fee, transaction.getFee());
        assertEquals(reference == null
                ? Integer.toString(buildTransaction(accountIban, amount, date, description, fee, reference).hashCode())
                : reference, transaction.getReference());
    }

    private Transaction buildTransaction(String accountIban, BigDecimal amount, OffsetDateTime date, String description,
            BigDecimal fee, String reference) {
        Transaction transaction = new Transaction();
        transaction.setAccountIban(accountIban);
        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setDescription(description);
        transaction.setFee(fee);
        transaction.setReference(reference);
        return transaction;
    }

}
