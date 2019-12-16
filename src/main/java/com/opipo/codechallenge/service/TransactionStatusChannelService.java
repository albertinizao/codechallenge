package com.opipo.codechallenge.service;

import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse;

public interface TransactionStatusChannelService {
    TransactionStatusResponse calculateAmounts(ChannelEnum channel, TransactionStatusResponse response,
            TransactionEntity transactionEntity);
}
