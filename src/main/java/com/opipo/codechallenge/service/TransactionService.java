package com.opipo.codechallenge.service;

import java.util.List;

import com.opipo.codechallenge.repository.model.SortType;
import com.opipo.web.api.model.Transaction;

public interface TransactionService {

    /**
     * Save a transaction. If the amount is lt 0, its not allowed
     * 
     * @param transaction
     */
    void save(Transaction transaction);

    List<Transaction> list(String iban, SortType sort);
    
    Boolean referenceExists(String reference);
    
    Boolean validateIban(String iban);
}
