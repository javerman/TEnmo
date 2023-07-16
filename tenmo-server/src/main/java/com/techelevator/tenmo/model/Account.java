package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

public class Account {
    @NotEmpty
    private int accountId;
    @NotEmpty
    private BigDecimal balance;
    private int ownerId;
    private List<Transfer> transferList;

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Transfer> getTransferList() {
        return transferList;
    }

    public void addTransfer(Transfer transfer) {
        transferList.add(transfer);
    }
}
