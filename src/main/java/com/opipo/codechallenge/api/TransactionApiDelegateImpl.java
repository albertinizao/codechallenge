package com.opipo.codechallenge.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.opipo.codechallenge.exception.InvalidRequestException;
import com.opipo.codechallenge.repository.model.SortType;
import com.opipo.codechallenge.service.TransactionService;
import com.opipo.web.api.TransactionApiDelegate;
import com.opipo.web.api.model.Transaction;

@Service
public class TransactionApiDelegateImpl implements TransactionApiDelegate {

    @Autowired
    private TransactionService transactionService;

    public ResponseEntity<Void> addTransaction(Transaction transaction) {
        validateIban(transaction.getAccountIban());
        transactionService.save(transaction);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    public ResponseEntity<List<Transaction>> getTransactions(String accountIban, String sort) {
        validateIban(accountIban);
        return new ResponseEntity<List<Transaction>>(
                transactionService.list(accountIban, sort == null ? null : SortType.fromValue(sort)), HttpStatus.OK);
    }

    private void validateIban(String accountIban) {
        if (!transactionService.validateIban(accountIban)) {
            throw new InvalidRequestException();
        }
    }

}
