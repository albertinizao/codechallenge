package com.opipo.codechallenge.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opipo.codechallenge.repository.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    Collection<TransactionEntity> findAllByAccountIban(String accountIban);
}
