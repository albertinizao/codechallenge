package com.opipo.codechallenge.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.opipo.codechallenge.service.TransactionStatusService;
import com.opipo.web.api.TransactionStatusApiDelegate;
import com.opipo.web.api.model.TransactionStatusRequest;
import com.opipo.web.api.model.TransactionStatusResponse;

@Service
public class TransactionStatusApiDelegateImpl implements TransactionStatusApiDelegate {

    @Autowired
    private TransactionStatusService transactionStatusService;

    public ResponseEntity<TransactionStatusResponse> transactionStatusPost(TransactionStatusRequest body) {
        return new ResponseEntity<TransactionStatusResponse>(transactionStatusService.execute(body), HttpStatus.OK);
    }
}
