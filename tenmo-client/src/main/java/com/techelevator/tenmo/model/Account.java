package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
//    private int userId;
    private BigDecimal balance;


    //Getters and Setters
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
//    public int getUserId() {
//        return userId;
//    }
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public void addCurrentBalance(BigDecimal money) {
        balance = balance.add(money);
    }

    public void subtractCurrentBalance(BigDecimal money) {
        if (balance.subtract(money).compareTo(BigDecimal.valueOf(0)) > 0) {
            balance = balance.subtract(money);
        } else {
            System.out.println("Insufficient Funds! Transaction cancelled");
        }
    }

    public boolean sendMoney(Account accountTo, BigDecimal money) {
        if (money.compareTo(BigDecimal.valueOf(0)) <= 0) {
            System.out.println("Cannot send 0 or negative amounts! Transaction cancelled");
            return false;
        }
        if (balance.subtract(money).compareTo(BigDecimal.valueOf(0)) > 0) {
            balance = balance.subtract(money);
            accountTo.balance = accountTo.balance.add(money);
            return true;
        } else {
            System.out.println("Insufficient Funds! Transaction cancelled");
            return false;
        }
    }

    public int countXX(String str) {
        int count = 0;

        for(int i = 0; i < str.length() - 1; i++) {
            if(str.substring(i, i + 2).equals("xx")) {
                count++;
            }
        }

        return count;
    }

}
