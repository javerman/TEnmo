package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account getAccountById(int id);
    BigDecimal getCurrentBalance(int accountId);
    List<Account> getListOfAccounts();

    List<Account> getListOfAccountsByUser(int userId);
    Account updateAccountBalance(Account account);

}
