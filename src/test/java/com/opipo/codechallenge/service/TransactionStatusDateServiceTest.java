package com.opipo.codechallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.MockitoAnnotations;

import com.opipo.web.api.model.TransactionStatusResponse.StatusEnum;

public abstract class TransactionStatusDateServiceTest {
    private TransactionStatusDateService transactionStatusDateService;

    @BeforeEach
    public void init() {
        this.transactionStatusDateService = buildTransactionStatusDateService();
        MockitoAnnotations.initMocks(this);
    }

    public abstract TransactionStatusDateService buildTransactionStatusDateService();

    @Test
    @DisplayName("Given date before today then get is before")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenBeforeTodayThenGetIsBefore() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(1l);
        assertTrue(transactionStatusDateService.isBeforeToday(localDateTime));
    }

    @Test
    @DisplayName("Given date before today then get is not before")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenTodayThenGetIsNotBefore() {
        LocalDateTime localDateTime = LocalDateTime.now();
        assertFalse(transactionStatusDateService.isBeforeToday(localDateTime));
    }

    @Test
    @DisplayName("Given date after today then get is not before")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenAfterTodayThenGetIsNotBefore() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(1l);
        assertFalse(transactionStatusDateService.isBeforeToday(localDateTime));
    }

    @Test
    @DisplayName("Given date today then get is not after")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenBeforeTodayThenGetIsAfter() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(1l);
        assertFalse(transactionStatusDateService.isAfterToday(localDateTime));
    }

    @Test
    @DisplayName("Given date today then get is not after")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenTodayThenGetIsNotAfter() {
        LocalDateTime localDateTime = LocalDateTime.now();
        assertFalse(transactionStatusDateService.isAfterToday(localDateTime));
    }

    @Test
    @DisplayName("Given date after today then get is after")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenAfterTodayThenGetIsNotAfter() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(1l);
        assertTrue(transactionStatusDateService.isAfterToday(localDateTime));
    }

    @Test
    @DisplayName("Given date before today then get Status Settled")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenBeforeTodayThenGetSettled() {
        OffsetDateTime localDateTime = OffsetDateTime.now();
        localDateTime = localDateTime.minusDays(1l);
        assertEquals(StatusEnum.SETTLED, transactionStatusDateService.buildStatus(localDateTime));
    }

    @Test
    @DisplayName("Given date today then get Status Pending")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenTodayThenGetPending() {
        OffsetDateTime localDateTime = OffsetDateTime.now();
        assertEquals(StatusEnum.PENDING, transactionStatusDateService.buildStatus(localDateTime));
    }

    @Test
    @DisplayName("Given date after today then get Status Future")
    @Execution(ExecutionMode.CONCURRENT)
    public void givenAfterTodayThenGeFuture() {
        OffsetDateTime localDateTime = OffsetDateTime.now();
        localDateTime = localDateTime.plusDays(1l);
        assertEquals(StatusEnum.FUTURE, transactionStatusDateService.buildStatus(localDateTime));
    }
}