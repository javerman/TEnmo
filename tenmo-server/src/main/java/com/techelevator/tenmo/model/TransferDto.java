package com.techelevator.tenmo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.relational.core.mapping.Embedded;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class TransferDto {
    @NotEmpty
    private int transferId;

    @NotEmpty
    private int typeId;
    @NotEmpty
    private int statusId;
    @NotEmpty
    private int accountFrom;
    @NotEmpty
    private int accountTo;
    @NotEmpty
    private BigDecimal amount;

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setTypeId(int id) {
        this.typeId = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
