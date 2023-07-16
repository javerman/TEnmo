package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(1,new BigDecimal(100),1);
    private static final Account ACCOUNT_2 = new Account(2,new BigDecimal(200),2);
    private static final Account ACCOUNT_3 = new Account(3,new BigDecimal(300),3);

    private JdbcAccountDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);

    }
    @Test
    public void invalid_user_returns_null() {
        Account account = sut.getAccountById(99);
        Assert.assertNull(account);
        account = sut.getAccountById(1);
        Assert.assertNull(account);
    }
  @Test
  public void update_account_balance() {
    Account accountToUpdate = sut.getAccountById(1);
    accountToUpdate.setAccountId(1);
    accountToUpdate.setBalance(new BigDecimal(101));
    accountToUpdate.setOwnerId(1);
    sut.updateAccountBalance(accountToUpdate);
    Account updatedAccount = sut.getAccountById(1);
    assertAccountMatch(accountToUpdate,updatedAccount);
    }
//  @Test
//  public void get_current_balance() {
//    BigDecimal account = sut.getCurrentBalance(1);
//    Assert.assertEquals(ACCOUNT_1,account);
//
//    account = sut.getCurrentBalance(2);
//    assertAccountMatch(ACCOUNT_2,new BigDecimal(account));
//    }

    private void assertAccountMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Assert.assertEquals(expected.getOwnerId(), actual.getOwnerId());
    }
}
