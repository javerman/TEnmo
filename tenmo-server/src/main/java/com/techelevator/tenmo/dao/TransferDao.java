package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {
    List<Transfer> getTransfersByPending();
    List<Transfer> getAllTransfers();

    List<Transfer> getTransfersByAccount(int accountId);
    List<Transfer> getTransferByAccountFrom(int accountFrom);
    List<Transfer> getTransferByAccountTo(int accountTo);
    Transfer getTransferByTransferId(int transferId);
    Transfer createTransfer (TransferDto transfer);
    
    List<Transfer> getListOfTransfersByUser(int userId);
    List<Transfer> getPendingRequestsByUser(int userId);
    Transfer updateTransferStatus(TransferDto transfer);
}
