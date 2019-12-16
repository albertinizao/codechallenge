package com.opipo.codechallenge.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Stream;

import com.opipo.codechallenge.repository.model.TransactionEntity;

public interface TransactionAmountService {

    BigDecimal calculateAmount(final BigDecimal amount, final BigDecimal fee);

    BigDecimal calculateAmount(final TransactionEntity transactionEntity);

    BigDecimal sumarize(final Collection<TransactionEntity> transactions);

    boolean checkSumIsGteZero(final BigDecimal actual, final BigDecimal total);

}
