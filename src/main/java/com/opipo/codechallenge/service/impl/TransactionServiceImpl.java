package com.opipo.codechallenge.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opipo.codechallenge.exception.AlreadyExistsException;
import com.opipo.codechallenge.exception.AmountIncorrectException;
import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.SortType;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionAmountService;
import com.opipo.codechallenge.service.TransactionService;
import com.opipo.web.api.model.Transaction;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionAmountService transactionAmountService;

    @Autowired
    private Comparator<TransactionEntity> transactionEntityAmountComparator;

    private static final String IBAN_PATTERN = "^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$";
    private static final Pattern IBAN_PATTERN_COMPILED = Pattern.compile(IBAN_PATTERN);

    @Override
    public void save(Transaction transaction) {
        if (referenceExists(transaction.getReference())) {
            throw new AlreadyExistsException();
        }
        TransactionEntity entity = modelMapper.map(transaction, TransactionEntity.class);
        if (transactionAmountService.checkSumIsGteZero(transactionAmountService.calculateAmount(entity),
                transactionAmountService
                        .sumarize(transactionRepository.findAllByAccountIban(entity.getAccountIban())))) {
            transactionRepository.save(entity);
        } else {
            throw new AmountIncorrectException();
        }
    }

    @Override
    public List<Transaction> list(String accountIban, SortType sort) {
        Stream<TransactionEntity> stream = accountIban == null ? transactionRepository.findAll().stream()
                : transactionRepository.findAllByAccountIban(accountIban).stream();
        return stream
                .sorted(SortType.ASC.equals(sort) ? transactionEntityAmountComparator
                        : Collections.reverseOrder(transactionEntityAmountComparator))
                .map(f -> modelMapper.map(f, Transaction.class)).collect(Collectors.toList());
    }

    public Boolean referenceExists(String reference) {
        return reference != null && transactionRepository.findById(reference).isPresent();
    }

    @Override
    public Boolean validateIban(String iban) {
        return iban == null || IBAN_PATTERN_COMPILED.matcher(iban).find();
    }

}
