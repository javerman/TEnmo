package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {
    @JsonProperty("transferId")
    private int transferId;
    @JsonProperty("transfer_type_id")
    private int type;
    @JsonProperty("transfer_status_id")
    private int statusId;
    //private String statusDescription;
    @JsonProperty("account_from")
    private int accountFrom;
    @JsonProperty("account_to")
    private int accountTo;
    @JsonProperty("amount")
    private BigDecimal amount;

    public Transfer (int transferId, int type, int statusId, String statusDescription, int accountFrom, int accountTo, BigDecimal amount){
        this.transferId = transferId;
        this.type = type;
        this.statusId = statusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer() {};

    public int getTransferId(){
        return transferId;
    }
    public void setTransferId(int transferId){
        this.transferId = transferId;
    }
    public int getType(){
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getStatusId(){
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
}
