package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Account getAccountById(int id) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;

    }

    public BigDecimal getCurrentBalance(int accountId) {
        BigDecimal currentBalance = BigDecimal.valueOf(0);
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        try {
            currentBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return currentBalance;
    }

    @Override
    public List<Account> getListOfAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        try {
            Account account = null;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while(result.next()) {
                account = mapRowToAccount(result);
                accounts.add(account);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return accounts;
    }


    @Override
    public List<Account> getListOfAccountsByUser(int id) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE account.user_id = ?;";
        try {
            Account account = null;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
            while(result.next()) {
                account = mapRowToAccount(result);
                accounts.add(account);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }

    @Override
    public Account updateAccountBalance(Account account) {
        Account updatedAccount = null;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, account.getBalance(), account.getAccountId());
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedAccount = getAccountById(account.getAccountId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return updatedAccount;
    }

    public Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setOwnerId(rowSet.getInt("user_id"));
        return account;

    }
}
