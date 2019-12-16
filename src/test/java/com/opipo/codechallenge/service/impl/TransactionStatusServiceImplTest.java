package com.opipo.codechallenge.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionStatusChannelService;
import com.opipo.codechallenge.service.TransactionStatusDateService;
import com.opipo.web.api.model.TransactionStatusRequest;
import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse;
import com.opipo.web.api.model.TransactionStatusResponse.StatusEnum;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionStatusServiceImpl test")
public class TransactionStatusServiceImplTest {

    @InjectMocks
    private TransactionStatusServiceImpl transactionStatusServiceImpl;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionStatusDateService transactionStatusDateService;

    @Mock
    private TransactionStatusChannelService transactionStatusChannelService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Given transactionStatusRequest with inexistent referente then return status invalid")
    void givenTransactionStatusRequestWithInexistentReferenceThenReturnInvalid() {
        String reference = "myId";
        TransactionStatusRequest transactionStatusRequest = new TransactionStatusRequest();
        transactionStatusRequest.setReference(reference);
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.empty());
        TransactionStatusResponse actual = transactionStatusServiceImpl.execute(transactionStatusRequest);
        assertNotNull(actual);
        assertEquals(reference, actual.getReference());
        assertEquals(StatusEnum.INVALID, actual.getStatus());
    }

    @Test
    @DisplayName("Given transactionStatusRequest with inexistent referente then return status invalid")
    void givenTransactionStatusRequestReferenceThenReturnTransactionStatusResponse() {
        String reference = "myId";
        OffsetDateTime date = OffsetDateTime.now();
        StatusEnum status = StatusEnum.FUTURE;
        ChannelEnum channel = ChannelEnum.ATM;
        TransactionStatusResponse actualResponsed = Mockito.mock(TransactionStatusResponse.class);
        TransactionStatusRequest transactionStatusRequest = new TransactionStatusRequest();
        transactionStatusRequest.setReference(reference);
        transactionStatusRequest.setChannel(channel);
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setReference(reference);
        transactionEntity.setDate(date);
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.of(transactionEntity));
        Mockito.when(transactionStatusDateService.buildStatus(date)).thenReturn(status);
        Mockito.when(transactionStatusChannelService.calculateAmounts(Mockito.eq(channel), Mockito.any(),
                Mockito.eq(transactionEntity))).thenReturn(actualResponsed);

        TransactionStatusResponse actual = transactionStatusServiceImpl.execute(transactionStatusRequest);
        assertEquals(actualResponsed, actual);
    }

}
