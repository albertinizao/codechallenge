package com.opipo.codechallenge.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionStatusChannelService;
import com.opipo.web.api.model.TransactionStatusRequest.ChannelEnum;
import com.opipo.web.api.model.TransactionStatusResponse;

@Service
public class TransactionStatusChannelServiceImpl implements TransactionStatusChannelService {
    public TransactionStatusResponse calculateAmounts(ChannelEnum channel, TransactionStatusResponse response,
            TransactionEntity transactionEntity) {
        switch (channel) {
        case INTERNAL:
            response.setAmount(transactionEntity.getAmount());
            response.setFee(transactionEntity.getFee());
            break;
        case ATM:
        case CLIENT:
        default:
            response.setAmount(transactionEntity.getAmount()
                    .subtract(Optional.ofNullable(transactionEntity.getFee()).orElse(BigDecimal.ZERO)));
            break;
        }
        return response;
    }
}
