package com.techelevator.tenmo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private int id;
    private String username;
    private List<Account> accountList = new ArrayList<>();
    private List<TransferDto> transferList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void addToAccountList(Account account) {
        accountList.add(account);
    }

    public User getUserById(int id) {
        return this;
    }

    public void deleteAccountList() {
        List<Account> removeAccounts = accountList;
        accountList.removeAll(removeAccounts);
    }

    public List<TransferDto> getTransferList() {
        return transferList;
    }

    public void addToTransferList(TransferDto transfer) {
        transferList.add(transfer);
    }

    public void deleteTransferList() {
        List<TransferDto> removeTransfer = transferList;
        transferList.removeAll(removeTransfer);
    }

}
