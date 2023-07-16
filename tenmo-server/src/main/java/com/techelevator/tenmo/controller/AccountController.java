package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
public class AccountController {
    private JdbcAccountDao accountDao;
    private JdbcUserDao userDao;


    public AccountController(JdbcAccountDao accountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;

    }

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        List<Account> accounts = accountDao.getListOfAccounts();
        return accounts;
    }
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public Account get(@PathVariable("id") int id) {
        Account account = accountDao.getAccountById(id);
        if(account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return account;
        }
    }

    @RequestMapping(path = "accounts/{id}", method = RequestMethod.PUT)
    public Account update(@RequestBody Account account, @PathVariable int id) {
        account.setAccountId(id);
        try {
            Account updatedAccount = accountDao.updateAccountBalance(account);
            return updatedAccount;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
    }

    @RequestMapping(path = "/users/{id}/accounts", method = RequestMethod.GET)
    public List<Account> listAccountsByUser(@PathVariable("id") int userId) {
        List<Account> accounts = accountDao.getListOfAccountsByUser(userId);
        if (accounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return accounts;
        }
    }

    @RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable("id") int accountId) {
        BigDecimal balance = accountDao.getCurrentBalance(accountId);
        if(balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return balance;
        }
    }


}
