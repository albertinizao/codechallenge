package com.opipo.codechallenge.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.opipo.codechallenge.exception.InvalidRequestException;
import com.opipo.codechallenge.repository.model.SortType;
import com.opipo.codechallenge.service.TransactionService;
import com.opipo.web.api.model.Transaction;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionApiDelegateImpl test")
public class TransactionApiDelegateImplTest {

    @InjectMocks
    private TransactionApiDelegateImpl transactionApiDelegate;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Given transaction then save it and return created")
    void givenTransactionThenSaveIt() {
        Transaction transaction = Mockito.mock(Transaction.class);
        String iban = "myIban";
        Mockito.when(transaction.getAccountIban()).thenReturn(iban);
        HttpStatus expectedStatus = HttpStatus.CREATED;

        Mockito.when(transactionService.validateIban(iban)).thenReturn(true);

        ResponseEntity<Void> response = transactionApiDelegate.addTransaction(transaction);
        Mockito.verify(transactionService).save(transaction);

        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    @DisplayName("Given transaction that provokes exception then return exception")
    void givenTransactionThenThrowsException() {
        Transaction transaction = Mockito.mock(Transaction.class);
        String iban = "myIban";
        Mockito.when(transaction.getAccountIban()).thenReturn(iban);

        Mockito.when(transactionService.validateIban(iban)).thenReturn(true);
        Mockito.doThrow(new IllegalArgumentException()).when(transactionService).save(transaction);

        assertThrows(IllegalArgumentException.class, () -> transactionApiDelegate.addTransaction(transaction));

    }

    @Test
    @DisplayName("Given accountIban and order then get list ordered")
    void givenAccountIbanAndOrderThenReturnList() {
        String iban = "myIban";
        SortType sort = SortType.ASC;
        List<Transaction> transactions = new ArrayList<>();
        Mockito.when(transactionService.validateIban(iban)).thenReturn(true);
        Mockito.when(transactionService.list(iban, sort)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> actual = transactionApiDelegate.getTransactions(iban, sort.toString());

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactions, actual.getBody());
    }

    @Test
    @DisplayName("Given accountIban and no order then get list unordered")
    void givenAccountIbanAndNoOrderThenReturnList() {
        String iban = "myIban";
        List<Transaction> transactions = new ArrayList<>();
        Mockito.when(transactionService.validateIban(iban)).thenReturn(true);
        Mockito.when(transactionService.list(iban, null)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> actual = transactionApiDelegate.getTransactions(iban, null);

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactions, actual.getBody());
    }

    @Test
    @DisplayName("Given accountIban and bad order then throw exception")
    void givenAccountIbanAndBadOrderThenThrowException() {
        String iban = "myIban";
        Mockito.when(transactionService.validateIban(iban)).thenReturn(true);
        assertThrows(InvalidRequestException.class, () -> transactionApiDelegate.getTransactions(iban, "fake"));
    }

    @Test
    @DisplayName("Given bad accountIban then throw exception")
    void givenBadAccountIbanThenThrowException() {
        String iban = "myIban";
        Mockito.when(transactionService.validateIban(iban)).thenReturn(false);
        assertThrows(InvalidRequestException.class, () -> transactionApiDelegate.getTransactions(iban, "fake"));
    }

}
