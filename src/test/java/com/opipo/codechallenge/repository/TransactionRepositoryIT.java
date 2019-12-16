package com.opipo.codechallenge.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.opipo.codechallenge.CodechallengeApplication;
import com.opipo.codechallenge.repository.model.TransactionEntity;

@SpringBootTest(classes = CodechallengeApplication.class)
@DisplayName("TransactionRepository Integration Test")
public class TransactionRepositoryIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void resetAll() {
        transactionRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Given one transacton persisted then get it")
    public void givenOneTransactionThenGetIt() {
        TransactionEntity t1 = buildTransactionEntity("accountIban", BigDecimal.valueOf(14.25d), OffsetDateTime.now(),
                "my desc", BigDecimal.valueOf(3.14d), "myId");
        TransactionEntity persisted = transactionRepository.save(t1);
        TransactionEntity actual = transactionRepository.getOne(persisted.getReference());
        assertNotNull(persisted.getReference());
        assertEquals(t1, actual);
    }

    @Test
    @Transactional
    @DisplayName("Given multiple transactions with different accountIban then find one by accountIban")
    public void givenMultipleTransactionThenGetOnlyOne() {
        TransactionEntity t1 = buildTransactionEntity("accountIban", BigDecimal.valueOf(14.25d), OffsetDateTime.now(),
                "my desc", BigDecimal.valueOf(3.14d), "myId");
        TransactionEntity t2 = buildTransactionEntity("accountIban2", BigDecimal.valueOf(22.25d), OffsetDateTime.now(),
                "my desc2", BigDecimal.valueOf(2.14d), "My reff");
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        Collection<TransactionEntity> transactions = transactionRepository.findAllByAccountIban(t1.getAccountIban());
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertTrue(transactions.contains(t1));
    }

    @Test
    @Transactional
    @DisplayName("Given multiple transactions then find all by accountIban")
    public void givenMultipleTransactionThenGetThem() {
        TransactionEntity t1 = buildTransactionEntity("accountIban", BigDecimal.valueOf(14.25d), OffsetDateTime.now(),
                "my desc", BigDecimal.valueOf(3.14d), "myId");
        TransactionEntity t2 = buildTransactionEntity(t1.getAccountIban(), BigDecimal.valueOf(22.25d),
                OffsetDateTime.now(), "my desc2", BigDecimal.valueOf(2.14d), "My reff");
        TransactionEntity t3 = buildTransactionEntity("accountIban3", BigDecimal.valueOf(14.25d), OffsetDateTime.now(),
                "my desc", BigDecimal.valueOf(3.14d), "myId2");
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        transactionRepository.save(t3);
        Collection<TransactionEntity> transactions = transactionRepository.findAllByAccountIban(t1.getAccountIban());
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(t1));
        assertTrue(transactions.contains(t2));
    }

    private TransactionEntity buildTransactionEntity(String accountIban, BigDecimal amount, OffsetDateTime date,
            String description, BigDecimal fee, String reference) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountIban(accountIban);
        transactionEntity.setAmount(amount);
        transactionEntity.setDate(date);
        transactionEntity.setDescription(description);
        transactionEntity.setFee(fee);
        transactionEntity.setReference(reference);
        return transactionEntity;
    }

}
