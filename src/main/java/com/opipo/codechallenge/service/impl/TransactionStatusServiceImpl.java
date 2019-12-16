package com.opipo.codechallenge.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionStatusChannelService;
import com.opipo.codechallenge.service.TransactionStatusDateService;
import com.opipo.codechallenge.service.TransactionStatusService;
import com.opipo.web.api.model.TransactionStatusRequest;
import com.opipo.web.api.model.TransactionStatusResponse;
import com.opipo.web.api.model.TransactionStatusResponse.StatusEnum;

@Service
public class TransactionStatusServiceImpl implements TransactionStatusService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionStatusDateService transactionStatusDateService;

    @Autowired
    private TransactionStatusChannelService transactionStatusChannelService;

    public TransactionStatusResponse execute(TransactionStatusRequest request) {
        Optional<TransactionEntity> transaction = transactionRepository.findById(request.getReference());
        if (!transaction.isPresent()) {
            return new TransactionStatusResponse().reference(request.getReference()).status(StatusEnum.INVALID);
        } else {
            return transactionStatusChannelService.calculateAmounts(
                    request.getChannel(), new TransactionStatusResponse().reference(transaction.get().getReference())
                            .status(transactionStatusDateService.buildStatus(transaction.get().getDate())),
                    transaction.get());
        }
    }

}
