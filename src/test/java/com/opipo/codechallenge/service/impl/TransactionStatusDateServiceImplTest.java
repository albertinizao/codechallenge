package com.opipo.codechallenge.service.impl;

import org.junit.jupiter.api.DisplayName;

import com.opipo.codechallenge.service.TransactionStatusDateService;
import com.opipo.codechallenge.service.TransactionStatusDateServiceTest;

@DisplayName("TransactionStatusDateserviceImpl (extens of TransactionStatusDateServiceTest) test")
public class TransactionStatusDateServiceImplTest extends TransactionStatusDateServiceTest {

    @Override
    public TransactionStatusDateService buildTransactionStatusDateService() {
        return new TransactionStatusDateServiceImpl();
    }

}
