package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {
    //private int transferId;

    private int transferId;

    private int typeId;

    private int statusId;
   // private String statusDescription;

    private BigDecimal amount;

   // private int descriptionId;
    private int accountTo;

    private int accountFrom;

    public int getTransferId(){
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int type) {
        this.typeId = type;
    }

    public int getStatusId() {
        return statusId;
    }

//    public void setStatusDescription(String statusDescription) {
//        this.statusDescription = statusDescription;
//    }

    public void setTransferAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
//    public void setDescriptionId(int descriptionId) {
//        this.descriptionId = descriptionId;
  //  }
    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }
    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountFrom() {
        return accountFrom;
    }


}