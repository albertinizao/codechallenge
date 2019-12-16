package com.opipo.codechallenge.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionAmountService;

@Service
public class TransactionAmountServiceImpl implements TransactionAmountService {

    @Override
    public BigDecimal calculateAmount(final BigDecimal amount, final BigDecimal fee) {
        return Optional.ofNullable(amount).orElseThrow(IllegalArgumentException::new)
                .subtract(Optional.ofNullable(fee).orElseGet(() -> BigDecimal.ZERO));
    }

    @Override
    public BigDecimal calculateAmount(TransactionEntity transactionEntity) {
        return calculateAmount(transactionEntity.getAmount(), transactionEntity.getFee());
    }

    @Override
    public BigDecimal sumarize(final Collection<TransactionEntity> transactions) {
        return transactions.stream().map(f -> calculateAmount(f)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean checkSumIsGteZero(final BigDecimal actual, final BigDecimal total) {
        return Optional.ofNullable(actual).orElse(BigDecimal.ZERO)
                .add(Optional.ofNullable(total).orElse(BigDecimal.ZERO)).compareTo(BigDecimal.ZERO) >= 0;
    }
}
