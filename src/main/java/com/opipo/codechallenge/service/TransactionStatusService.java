package com.opipo.codechallenge.service;

import com.opipo.web.api.model.TransactionStatusRequest;
import com.opipo.web.api.model.TransactionStatusResponse;

public interface TransactionStatusService {
    TransactionStatusResponse execute(TransactionStatusRequest request);
}
