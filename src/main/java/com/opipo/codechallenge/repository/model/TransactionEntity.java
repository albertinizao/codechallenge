package com.opipo.codechallenge.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "TRANSACTIONS")
public class TransactionEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1974964074131344472L;

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
