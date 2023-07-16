package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests {
    private static final Transfer TRANSFER_1 = new Transfer(1,1,1,"This is a test",1,2,new BigDecimal(10));
    private static final Transfer TRANSFER_2 = new Transfer(2,2,2,"This is another test",1,2,new BigDecimal(20));


    private JdbcTransferDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void list_of_transfers() {
        List<Transfer> transfers= sut.getAllTransfers();

        Assert.assertNotNull(transfers);
        Assert.assertEquals(2, transfers.size());
        Assert.assertEquals(TRANSFER_1, transfers.get(0));
        Assert.assertEquals(TRANSFER_2, transfers.get(1));
    }
    @Test
    public void transfer_success_test() {

    }
    private void assertTransferMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getStatusId(), actual.getStatusId());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
    }
}
