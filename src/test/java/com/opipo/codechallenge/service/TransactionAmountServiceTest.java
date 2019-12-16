package com.opipo.codechallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;

import com.opipo.codechallenge.repository.model.TransactionEntity;

public abstract class TransactionAmountServiceTest<T extends TransactionAmountService> {

    private T transactionAmountService;

    @BeforeEach
    public void init() {
        transactionAmountService = buildTransactionAmountService();
        MockitoAnnotations.initMocks(this);
    }

    abstract protected T buildTransactionAmountService();

    private static Stream<Arguments> provideAmounts() {
        return Stream.of(Arguments.of(10d, 0.1d, 9.9d), Arguments.of(10d, 0d, 10d), Arguments.of(10d, 10d, 0d),
                Arguments.of(10.1d, 0.1d, 10d), Arguments.of(-10d, 0.1d, -10.1d), Arguments.of(-4d, 8d, -12d),
                Arguments.of(0d, 0.1d, -0.1d), Arguments.of(10d, null, 10d), Arguments.of(4d, 8d, -4d));
    }

    private static Stream<Arguments> provideTransactions() {
        return Stream.of(
                Arguments.of(-5d,
                        new TransactionEntity[] {buildTransactionEntity(4d, 8d), buildTransactionEntity(15d, 16d)}),
                Arguments.of(-43d,
                        new TransactionEntity[] {buildTransactionEntity(-4d, 8d), buildTransactionEntity(-15d, 16d)}),
                Arguments.of(-30d,
                        new TransactionEntity[] {buildTransactionEntity(-10d, 2d), buildTransactionEntity(-15d, 3d)}),
                Arguments.of(-35d,
                        new TransactionEntity[] {buildTransactionEntity(-4d, null), buildTransactionEntity(-15d, 16d)}),
                Arguments.of(45d,
                        new TransactionEntity[] {buildTransactionEntity(10d, 0.1d), buildTransactionEntity(11d, 0.2d),
                                buildTransactionEntity(12d, 0.3d), buildTransactionEntity(13d, 0.4d)}),
                Arguments.of(4d, new TransactionEntity[] {buildTransactionEntity(23d, 19d)}));
    }

    private static Stream<Arguments> provideSums() {
        return Stream.of(Arguments.of(10d, 1d, true), Arguments.of(0d, 10d, true), Arguments.of(-10d, 1d, false),
                Arguments.of(1d, -10d, false), Arguments.of(null, -1d, false), Arguments.of(1d, null, true),
                Arguments.of(1d, 1d, true), Arguments.of(1d, -1d, true));
    }

    private static TransactionEntity buildTransactionEntity(Double amount, Double fee) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(amount == null ? null : BigDecimal.valueOf(amount));
        transactionEntity.setFee(fee == null ? null : BigDecimal.valueOf(fee));
        return transactionEntity;
    }

    @ParameterizedTest(name = "Given amount {0} and fee {1}, {2} is expected")
    @MethodSource("provideAmounts")
    @DisplayName("Given amount and fee, then check the sum")
    @Execution(ExecutionMode.CONCURRENT)
    void calculateAmount(final Double amount, final Double fee, final Double result) {
        assertEquals(BigDecimal.valueOf(result), transactionAmountService.calculateAmount(
                amount == null ? null : BigDecimal.valueOf(amount), fee == null ? null : BigDecimal.valueOf(fee)));
    }

    @ParameterizedTest(name = "Given transaction with amount {0} and fee {1}, {2} is expected")
    @MethodSource("provideAmounts")
    @DisplayName("Given Transaction with amount and fee, then check the sum")
    @Execution(ExecutionMode.CONCURRENT)
    void calculateAmountWithTransaction(final Double amount, final Double fee, final Double result) {
        assertEquals(BigDecimal.valueOf(result),
                transactionAmountService.calculateAmount(buildTransactionEntity(amount, fee)));
    }

    @Test
    @DisplayName("Given null amount and any fee, IllegalArgumentException is expected")
    @Execution(ExecutionMode.CONCURRENT)
    void givenNullAmountThenThrowNullPointer() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionAmountService.calculateAmount(null, BigDecimal.ONE));
    }

    @ParameterizedTest
    @MethodSource("provideTransactions")
    @DisplayName("Given transaction stream, then sumarize all")
    @Execution(ExecutionMode.CONCURRENT)
    void givenTransactionsThenSumarizeIt(final Double result, final TransactionEntity[] transactionArray) {
        assertEquals(BigDecimal.valueOf(result), transactionAmountService.sumarize(Arrays.asList(transactionArray)));
    }

    @ParameterizedTest
    @MethodSource("provideSums")
    @DisplayName("Given two number, check if the sum are gt 0")
    @Execution(ExecutionMode.CONCURRENT)
    void givenNumberThenSumarizeItAndCheckGt0(final Double amount1, final Double amount2, final Boolean result) {
        assertEquals(result,
                transactionAmountService.checkSumIsGteZero(amount1 == null ? null : BigDecimal.valueOf(amount1),
                        amount2 == null ? null : BigDecimal.valueOf(amount2)));
    }
}
