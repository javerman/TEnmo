package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferDto {
    @JsonProperty("transferId")
    private int transferId;
    @JsonProperty("transfer_type_id")
    private int typeId;
    @JsonProperty("transfer_status_id")
    private int statusId;
    // private String statusDescription;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("account_to")
    // private int descriptionId;
    private int accountTo;
    @JsonProperty("account_from")
    private int accountFrom;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Transfer createTransfer() {
        Transfer transfer = new Transfer();
        transfer.setTransferId(transferId);
        transfer.setTypeId(typeId);
        transfer.setAccountTo(accountTo);
        transfer.setAccountFrom(accountFrom);
        transfer.setTransferAmount(amount);
        transfer.setStatusId(statusId);

        return transfer;

    }
}
