package com.opipo.codechallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.impl.TransactionStatusChannelServiceImpl;
import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse;

@DisplayName("TransactionStatusChannelService test")
public class TransactionStatusChannelServiceTest {

    private TransactionStatusChannelService transactionStatusChannelService;

    @BeforeEach
    void init() {
        this.transactionStatusChannelService = new TransactionStatusChannelServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Given internal channel and transactionEntity and transactionStatusResponse then fill it with amount and fee")
    void givenInternalAndTransacionEntityAndTransactionStatusResponseThenSetAmountAndFee() {
        ChannelEnum channel = ChannelEnum.INTERNAL;
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal amount = BigDecimal.valueOf(14.2d);
        BigDecimal fee = BigDecimal.valueOf(2.1d);
        Mockito.when(transactionEntity.getAmount()).thenReturn(amount);
        Mockito.when(transactionEntity.getFee()).thenReturn(fee);
        TransactionStatusResponse response = new TransactionStatusResponse();
        TransactionStatusResponse actual = transactionStatusChannelService.calculateAmounts(channel, response,
                transactionEntity);
        assertEquals(actual.getAmount(), amount);
        assertEquals(actual.getFee(), fee);
    }

    @Test
    @DisplayName("Given ATM channel and transactionEntity and transactionStatusResponse then fill it with amout that is amount-fee")
    void givenAtmAndTransacionEntityAndTransactionStatusResponseThenSetAmount() {
        ChannelEnum channel = ChannelEnum.ATM;
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal amount = BigDecimal.valueOf(14.2d);
        BigDecimal fee = BigDecimal.valueOf(2.1d);
        Mockito.when(transactionEntity.getAmount()).thenReturn(amount);
        Mockito.when(transactionEntity.getFee()).thenReturn(fee);
        TransactionStatusResponse response = new TransactionStatusResponse();
        TransactionStatusResponse actual = transactionStatusChannelService.calculateAmounts(channel, response,
                transactionEntity);
        assertEquals(actual.getAmount(), amount.subtract(fee));
    }

    @Test
    @DisplayName("Given Client channel and transactionEntity and transactionStatusResponse then fill it with amout that is amount-fee")
    void givenClientAndTransacionEntityAndTransactionStatusResponseThenSetAmount() {
        ChannelEnum channel = ChannelEnum.CLIENT;
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal amount = BigDecimal.valueOf(14.2d);
        BigDecimal fee = BigDecimal.valueOf(2.1d);
        Mockito.when(transactionEntity.getAmount()).thenReturn(amount);
        Mockito.when(transactionEntity.getFee()).thenReturn(fee);
        TransactionStatusResponse response = new TransactionStatusResponse();
        TransactionStatusResponse actual = transactionStatusChannelService.calculateAmounts(channel, response,
                transactionEntity);
        assertEquals(actual.getAmount(), amount.subtract(fee));
    }

    @Test
    @DisplayName("Given ATM channel and transactionEntity without fee and transactionStatusResponse then fill it with amout that is amount")
    void givenAtmAndTransacionEntityWithoutFeeAndTransactionStatusResponseThenSetAmount() {
        ChannelEnum channel = ChannelEnum.ATM;
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal amount = BigDecimal.valueOf(14.2d);
        Mockito.when(transactionEntity.getAmount()).thenReturn(amount);
        TransactionStatusResponse response = new TransactionStatusResponse();
        TransactionStatusResponse actual = transactionStatusChannelService.calculateAmounts(channel, response,
                transactionEntity);
        assertEquals(actual.getAmount(), amount);
    }

    @Test
    @DisplayName("Given CLIENT channel and transactionEntity without fee and transactionStatusResponse then fill it with amout that is amount")
    void givenClientAndTransacionEntityWithoutFeeAndTransactionStatusResponseThenSetAmount() {
        ChannelEnum channel = ChannelEnum.CLIENT;
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal amount = BigDecimal.valueOf(14.2d);
        Mockito.when(transactionEntity.getAmount()).thenReturn(amount);
        TransactionStatusResponse response = new TransactionStatusResponse();
        TransactionStatusResponse actual = transactionStatusChannelService.calculateAmounts(channel, response,
                transactionEntity);
        assertEquals(actual.getAmount(), amount);
    }
}
