package com.opipo.codechallenge.it.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.web.api.model.TransactionStatusRequest;
import com.opipo.web.api.model.TransactionStatusResponse;

import io.cucumber.java.Before;
import io.cucumber.java8.En;

public class TransactionStatusSteps implements En {
    private static final String ENDPOINT = "http://localhost:8080/transaction-status";

    @Autowired
    private TransactionRepository transactionRepository;

    private RestTemplate restTemplate;

    private Random random = new Random();

    private String reference = "ref" + random.nextInt();

    private TransactionEntity transactionEntity;

    private List<ResponseEntity<TransactionStatusResponse>> responseEntity = new ArrayList<>();

    private List<HttpStatus> httpStatus = new ArrayList<>();

    private List<Executable> calls = new ArrayList<>();

    private Boolean executed = false;

    @Before
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public TransactionStatusSteps() {
        Given("A transaction that is not stored in our system", () -> {
            transactionRepository.deleteAll();
            transactionEntity = buildTransactionEntity(reference);
        });

        Given("A transaction that is stored in our system", () -> {
            transactionRepository.deleteAll();
            transactionEntity = buildTransactionEntity(reference);
            transactionRepository.save(transactionEntity);
        });

        When("I check the status from {word} or {word} channel", (String channel1, String channel2) -> {
            calls.add(() -> storeTransactionStatusResponse(channel1));
            calls.add(() -> storeTransactionStatusResponse(channel2));
            executed = false;
        });

        When("I check the status from {word} channel", (String channel) -> {
            if ("any".equalsIgnoreCase(channel)) {
                Arrays.stream(TransactionStatusRequest.ChannelEnum.values())
                        .forEach(c -> calls.add(() -> storeTransactionStatusResponse(c.toString())));
            } else {
                calls.add(() -> storeTransactionStatusResponse(channel));
            }
            executed = false;
        });

        When("the transaction date is before today", () -> {
            transactionEntity.setDate(OffsetDateTime.now().minusDays(1l));
            transactionRepository.save(transactionEntity);
        });

        When("the transaction date is equals to today", () -> {
            transactionEntity.setDate(OffsetDateTime.now());
            transactionRepository.save(transactionEntity);
        });

        When("the transaction date is greater than today", () -> {
            transactionEntity.setDate(OffsetDateTime.now().plusDays(1l));
            transactionRepository.save(transactionEntity);
        });

        Then("The system returns the status {string}", (String status) -> {
            getResponseEntity().stream().forEach(
                    c -> assertEquals(TransactionStatusResponse.StatusEnum.valueOf(status), c.getBody().getStatus()));
        });

        Then("the amount", () -> {
            getResponseEntity().stream()
                    .forEach(c -> assertEquals(transactionEntity.getAmount(), c.getBody().getAmount()));
        });

        Then("the fee", () -> {
            getResponseEntity().stream().forEach(c -> assertEquals(transactionEntity.getFee(), c.getBody().getFee()));
        });

        Then("the amount substracting the fee", () -> {
            getResponseEntity().stream()
                    .forEach(c -> assertEquals(transactionEntity.getAmount().subtract(transactionEntity.getFee()),
                            c.getBody().getAmount()));
        });
    }

    private TransactionEntity buildTransactionEntity(String reference) {
        TransactionEntity te = new TransactionEntity();
        te.setAccountIban("iban" + random.nextInt());
        te.setAmount(BigDecimal.valueOf(random.nextDouble()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        te.setDate(OffsetDateTime.now());
        te.setDescription("myDescription" + random.nextInt());
        te.setFee(BigDecimal.valueOf(0.1d).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        te.setReference(reference);
        return te;
    }

    private void storeTransactionStatusResponse(String channel) {
        responseEntity.add(callTransactionStatus(channel));
    }

    private ResponseEntity<TransactionStatusResponse> callTransactionStatus(String channel) {
        TransactionStatusRequest request = new TransactionStatusRequest().reference(transactionEntity.getReference())
                .channel(TransactionStatusRequest.ChannelEnum.fromValue(channel));
        return restTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<TransactionStatusRequest>(request),
                TransactionStatusResponse.class);
    }

    private List<ResponseEntity<TransactionStatusResponse>> getResponseEntity() {
        try {
            if (!executed) {
                for (Executable executable : calls) {
                    executable.execute();
                }
                executed = true;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
}
