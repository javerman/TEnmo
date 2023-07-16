package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransferController {
    private JdbcTransferDao transferDao;
    public TransferController(JdbcTransferDao dao){
        this.transferDao = dao;
    }

    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable("id")int id) {
        Transfer transfer = transferDao.getTransferByTransferId(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfer;
        }
    }

    @RequestMapping(value = "/accounts/{id}/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfersByAccount(@PathVariable("id") int id) {
        List<Transfer> transfers = transferDao.getTransfersByAccount(id);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return transfers;
        }
    }

    @RequestMapping(value= "/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransfersByPending(){
        List<Transfer> transfers = transferDao.getTransfersByPending();
        return transfers;
    }


    @RequestMapping(value = "/transfer/from/{from}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountFrom(@PathVariable  int from){
        List<Transfer> transfers = transferDao.getTransferByAccountFrom(from);
        return transfers;
    }

    @RequestMapping(value = "/transfer/to/{to}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountTo(@PathVariable int to) {
        List<Transfer> transfers = transferDao.getTransferByAccountTo(to);
        return transfers;
    }

    @RequestMapping(path = "/users/{id}/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUser(@PathVariable("id") int userId) {
        List<Transfer> transfers = transferDao.getListOfTransfersByUser(userId);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return transfers;
        }
    }

    @RequestMapping(path = "/users/{id}/pendingtransfers", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfersByUser(@PathVariable("id") int userId) {
        List<Transfer> transfers = transferDao.getPendingRequestsByUser(userId);
        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return transfers;
        }
    }

    @RequestMapping(path = "transfer/{id}", method = RequestMethod.PUT)
    public Transfer update(@RequestBody TransferDto transfer, @PathVariable int id) {
        transfer.setTransferId(id);
        try {
            Transfer updatedTransfer = transferDao.updateTransferStatus(transfer);
            return updatedTransfer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void newTransfer(@RequestBody TransferDto newTransfer) {
        try {
            Transfer transfer = transferDao.createTransfer(newTransfer);
            if (transfer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer creation failed.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer creation failed.");
        }
    }

}
