package com.opipo.codechallenge.it.steps;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.web.api.model.Transaction;
import com.opipo.web.api.model.TransactionTesting;

import io.cucumber.java.Before;
import io.cucumber.java8.En;

public class TransactionSteps implements En {
    private static final String ENDPOINT = "http://localhost:8080/transaction";

    @Autowired
    private TransactionRepository transactionRepository;

    private RestTemplate restTemplate;

    private Transaction transaction;

    private TransactionEntity transactionStored;

    private ResponseEntity<Void> createResponse;

    private ResponseEntity<Transaction[]> listResponse;

    private Collection<Transaction> transactionsPersisted = new ArrayList<>();

    private HttpStatus errorStatus;

    private Random random = new Random();

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public TransactionSteps() {
        Given("Transaction with reference {word} is not stored", (String reference) -> {
            transactionRepository.findById(reference).ifPresent(c -> transactionRepository.delete(c));
            assertFalse(transactionRepository.findById(reference).isPresent());
        });

        Given("Transaction with reference {word} is stored", (String reference) -> {
            if (!transactionRepository.findById(reference).isPresent()) {
                transactionStored = buildTransactionEntity(reference);
                transactionRepository.save(transactionStored);
            }
        });

        Given("Transaction is stored", (TransactionTesting transaction) -> {
            TransactionEntity te = modelMapper.map(transaction, TransactionEntity.class);
            transactionRepository.save(te);
        });

        Given("No transaction stored", () -> {
            transactionRepository.deleteAll();
            assertTrue(transactionRepository.findAll().isEmpty());
        });
        Given("Multiple transactions persisted with account_iban {word}", (String iban) -> {
            transactionsPersisted.add(buildAndPersistTransaction(1, iban));
            transactionsPersisted.add(buildAndPersistTransaction(2, iban));
            transactionsPersisted.add(buildAndPersistTransaction(3, iban));
        });

        When("I save the transaction", (TransactionTesting transaction) -> {
            this.transaction = transaction;
            try {
                createResponse = restTemplate.exchange(ENDPOINT, HttpMethod.POST,
                        new HttpEntity<Transaction>(transaction), Void.class);
            } catch (HttpClientErrorException e) {
                errorStatus = e.getStatusCode();
            }
        });

        When("I list the transactions", () -> {
            restTemplateList(null, null);
        });

        When("I list the transactions with account_iban {word}", (String accountIban) -> {
            restTemplateList(accountIban, null);
        });

        When("I list the transactions with sort {word}", (String sortType) -> {
            restTemplateList(null, sortType);
        });

        When("I list the transactions with account_iban {word} and sort {word}", (String accountIban, String sort) -> {
            restTemplateList(accountIban, sort);
        });

        Then("the system returns the http-status {int}", (Integer int1) -> {
            assertEquals(HttpStatus.valueOf(int1),
                    errorStatus == null
                            ? createResponse == null ? listResponse.getStatusCode() : createResponse.getStatusCode()
                            : errorStatus);
        });

        Then("the transaction with reference {word} is stored", (String reference) -> {
            assertTrue(transactionRepository.findById(reference).isPresent());
        });

        Then("one transaction is stored", () -> {
            assertFalse(transactionRepository.findAll().isEmpty());
        });

        Then("the transaction stored is the same", () -> {
            TransactionEntity te = transactionRepository.findById(transaction.getReference()).get();
            assertAll(() -> assertEquals(transaction.getAccountIban(), te.getAccountIban()),
                    () -> assertEquals(transaction.getAmount() == null ? null : transaction.getAmount().doubleValue(),
                            te.getAmount() == null ? null : te.getAmount().doubleValue()),
                    () -> assertEquals(transaction.getDate() == null ? null : transaction.getDate().toLocalDate(),
                            te.getDate() == null ? null : te.getDate().toLocalDate()),
                    () -> assertEquals(transaction.getDescription(), te.getDescription()),
                    () -> assertEquals(transaction.getFee() == null ? null : transaction.getFee().doubleValue(),
                            te.getFee() == null ? null : te.getFee().doubleValue()),
                    () -> assertEquals(transaction.getReference(), te.getReference()));
        });

        Then("the transaction stored is not modified", () -> {
            TransactionEntity te = transactionRepository.findById(transaction.getReference()).get();
            assertAll(() -> assertEquals(transactionStored.getAccountIban(), te.getAccountIban()),
                    () -> assertEquals(
                            transactionStored.getAmount() == null ? null : transactionStored.getAmount().doubleValue(),
                            te.getAmount() == null ? null : te.getAmount().doubleValue()),
                    () -> assertEquals(
                            transactionStored.getDate() == null ? null : transactionStored.getDate().toLocalDate(),
                            te.getDate() == null ? null : te.getDate().toLocalDate()),
                    () -> assertEquals(transactionStored.getDescription(), te.getDescription()),
                    () -> assertEquals(
                            transactionStored.getFee() == null ? null : transactionStored.getFee().doubleValue(),
                            te.getFee() == null ? null : te.getFee().doubleValue()),
                    () -> assertEquals(transactionStored.getReference(), te.getReference()));
        });

        Then("the transaction list is empty", () -> {
            assertTrue(Arrays.asList(listResponse.getBody()).isEmpty());
        });

        Then("the transaction list has the elements", () -> {
            assertNotNull(listResponse);
            assertNotNull(listResponse.getBody());
            List<String> idsResponse = Arrays.stream(listResponse.getBody()).map(t -> t.getReference())
                    .collect(Collectors.toList());
            List<String> idsPersisted = transactionsPersisted.stream().map(t -> t.getReference())
                    .collect(Collectors.toList());
            assertEquals(idsPersisted.size(), idsResponse.size());
            assertTrue(idsPersisted.containsAll(idsResponse));
            assertTrue(idsResponse.containsAll(idsPersisted));
        });

        Then("the transation list is ordered asc", () -> {
            List<BigDecimal> amountResponse = Arrays.stream(listResponse.getBody()).map(t -> t.getAmount())
                    .collect(Collectors.toList());
            List<BigDecimal> amountResponseOrdered = Arrays.stream(listResponse.getBody()).map(t -> t.getAmount())
                    .sorted().collect(Collectors.toList());
            for (int i = 0; i < amountResponse.size(); i++) {
                assertEquals(amountResponse.get(i), amountResponseOrdered.get(i));
            }
        });

        Then("the transation list is ordered desc", () -> {
            List<BigDecimal> amountResponse = Arrays.stream(listResponse.getBody()).map(t -> t.getAmount())
                    .collect(Collectors.toList());
            List<BigDecimal> amountResponseOrdered = Arrays.stream(listResponse.getBody()).map(t -> t.getAmount())
                    .sorted().collect(Collectors.toList());
            for (int i = 0; i < amountResponse.size(); i++) {
                assertEquals(amountResponse.get(i), amountResponseOrdered.get(amountResponseOrdered.size() - i - 1));
            }
        });
    }

    private TransactionEntity buildTransactionEntity(String reference) {
        TransactionEntity te = new TransactionEntity();
        te.setAccountIban("myAccount");
        te.setAmount(BigDecimal.ONE);
        te.setDate(OffsetDateTime.now());
        te.setDescription("myDescription");
        te.setFee(BigDecimal.TEN);
        te.setReference(reference);
        return te;
    }

    private TransactionEntity buildTransactionEntity(Integer position, String iban, BigDecimal amount) {
        TransactionEntity te = new TransactionEntity();
        te.setAccountIban(iban);
        te.setAmount(amount);
        te.setDate(OffsetDateTime.now());
        te.setDescription("myDescription" + position);
        te.setFee(BigDecimal.TEN);
        te.setReference(iban.hashCode() + "-" + position);
        return te;
    }

    private Transaction buildTransaction(Integer position, String iban, BigDecimal amount) {
        Transaction te = new Transaction();
        te.setAccountIban(iban);
        te.setAmount(amount);
        te.setDate(OffsetDateTime.now());
        te.setDescription("myDescription" + position);
        te.setFee(BigDecimal.TEN);
        te.setReference(iban.hashCode() + "-" + position);
        return te;
    }

    private void restTemplateList(String accountIban, String sort) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(ENDPOINT);
        if (accountIban != null) {
            url = url.queryParam("account_iban", accountIban);
        }
        if (sort != null) {
            url = url.queryParam("sort", sort);
        }
        try {
            listResponse = restTemplate.exchange(url.toUriString(), HttpMethod.GET,
                    new HttpEntity<Void>((MultiValueMap<String, String>) null), Transaction[].class);
        } catch (HttpClientErrorException e) {
            errorStatus = e.getStatusCode();
        }
    }

    private Transaction buildAndPersistTransaction(Integer position, String iban) {
        BigDecimal amount = BigDecimal.valueOf(random.nextDouble());
        transactionRepository.save(buildTransactionEntity(position, iban, amount));
        return buildTransaction(position, iban, amount);
    }

}
