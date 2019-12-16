package com.opipo.codechallenge.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse.StatusEnum;

public interface TransactionStatusDateService {
    StatusEnum buildStatus(OffsetDateTime transactionDate, ChannelEnum channel);

    Boolean isBeforeToday(LocalDateTime localTransactionDate);

    Boolean isAfterToday(LocalDateTime localTransactionDate);
}
