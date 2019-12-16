package com.opipo.codechallenge.repository.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TransactionEntityAmountComparator implements Comparator<TransactionEntity> {

    @Override
    public int compare(TransactionEntity o1, TransactionEntity o2) {
        return Optional.ofNullable(o1.getAmount()).orElse(BigDecimal.ZERO)
                .compareTo(Optional.ofNullable(o2.getAmount()).orElse(BigDecimal.ZERO));
    }
}
