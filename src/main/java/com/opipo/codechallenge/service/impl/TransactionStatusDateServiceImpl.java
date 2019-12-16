package com.opipo.codechallenge.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.opipo.codechallenge.service.TransactionStatusDateService;
import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse.StatusEnum;

@Service
public class TransactionStatusDateServiceImpl implements TransactionStatusDateService {

    public StatusEnum buildStatus(OffsetDateTime transactionDate, ChannelEnum channel) {
        LocalDateTime localTransactionDate = transactionDate.toLocalDateTime();
        return isBeforeToday(localTransactionDate) ? StatusEnum.SETTLED
                : isAfterToday(localTransactionDate) && !ChannelEnum.ATM.equals(channel) ? StatusEnum.FUTURE
                        : StatusEnum.PENDING;
    }

    public Boolean isBeforeToday(LocalDateTime localTransactionDate) {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfDay = now.atStartOfDay();
        return localTransactionDate.isBefore(startOfDay);
    }

    public Boolean isAfterToday(LocalDateTime localTransactionDate) {
        LocalDate now = LocalDate.now();
        LocalDateTime endOfDay = now.plusDays(1).atStartOfDay();
        return !localTransactionDate.isBefore(endOfDay);
    }

}
