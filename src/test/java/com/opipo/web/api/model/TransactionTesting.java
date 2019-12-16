package com.opipo.web.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionTesting extends Transaction {

    public void setDate(String date) {
        if (null != date && !date.isEmpty()) {
            super.setDate(OffsetDateTime.parse(date));
        }
    }

    public void setReference(String reference) {
        if (null != reference && !reference.isEmpty()) {
            super.setReference(reference);
        }
    }

    public void setAccountIban(String accountIban) {
        if (null != accountIban && !accountIban.isEmpty()) {
            super.setAccountIban(accountIban);
        }
    }

    public void setAmount(BigDecimal amount) {
        if (null != amount) {
            super.setAmount(amount);
        }
    }

    public void setFee(BigDecimal fee) {
        if (null != fee) {
            super.setFee(fee);
        }
    }

    public void setDescription(String description) {
        if (null != description && !description.isEmpty()) {
            super.setDescription(description);
        }
    }

}
