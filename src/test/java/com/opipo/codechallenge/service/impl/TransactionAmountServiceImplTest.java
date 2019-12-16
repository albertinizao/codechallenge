package com.opipo.codechallenge.service.impl;

import org.junit.jupiter.api.DisplayName;

import com.opipo.codechallenge.service.TransactionAmountServiceTest;

@DisplayName("TransactionAmountServiceImpl (extends TransactionAmountServiceTest) test")
public class TransactionAmountServiceImplTest extends TransactionAmountServiceTest<TransactionAmountServiceImpl> {

    @Override
    protected TransactionAmountServiceImpl buildTransactionAmountService() {
        return new TransactionAmountServiceImpl();
    }

}
