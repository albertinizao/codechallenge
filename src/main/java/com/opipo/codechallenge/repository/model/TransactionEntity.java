package com.opipo.codechallenge.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "TRANSACTIONS")
public class TransactionEntity implements Serializable {

    @Id
    private String reference;

    @NotNull
    private String accountIban;

    private OffsetDateTime date;

    @NotNull
    private BigDecimal amount;

    private BigDecimal fee;

    private String description;
}
