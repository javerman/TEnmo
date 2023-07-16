package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.util.BasicLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        transferService.setToken(currentUser.getToken());
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

	private void viewCurrentBalance() {
        User user = currentUser.getUser();
        Account[] accounts = null;
        try {
            accounts = restTemplate.getForObject(API_BASE_URL + "users/" + user.getId() + "/accounts", Account[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log(e.getMessage());
        }
        consoleService.printAccountBalances(accounts);
    	}

    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

	private void viewTransferHistory() {
        boolean viewTransfer = true;
        while (viewTransfer) {
            User user = currentUser.getUser();
            TransferDto[] transfers = null;

            consoleService.createUserAccountList(user);

            System.out.println("View Transfer History");
            System.out.println(user.getUsername() + " Accounts:");
            for (Account account : user.getAccountList()) {
                System.out.println(account.getAccountId());
            }
            int accountToId = consoleService.promptForInt("Which account do you want to view history? ");
            boolean onAccountList = false;
            Account selectedAccount = null;
            for (Account account : user.getAccountList()) {
                if (account.getAccountId() == accountToId) {
                    selectedAccount = account;
                    onAccountList = true;
                    break;
                }
            }
            if (!onAccountList) {
                user.deleteAccountList();
                System.out.println("Please enter valid account number");
                break;
            }
            try {
                transfers = restTemplate.getForObject(API_BASE_URL + "accounts/" + selectedAccount.getAccountId() + "/transfer", TransferDto[].class);
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
            consoleService.printTransferScreen(selectedAccount, transfers);
            TransferDto selectedTransfer = new TransferDto();
            boolean transferOnList = false;
            while (!transferOnList) {
                int selectedTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel) ");
                if (selectedTransferId == 0) {
                    user.deleteAccountList();
                    viewTransfer = false;
                    break;
                }
                for (TransferDto transfer : transfers) {
                    if (transfer.getTransferId() == selectedTransferId) {
                        selectedTransfer = transfer;
                        transferOnList = true;

                    }
                }
                if (!transferOnList) {
                    System.out.println("TransferId not on list!");
                    user.deleteAccountList();
                    viewTransfer = false;
                    break;
                }
            }
            if(!viewTransfer) {
                break;
            }
            boolean showDetails = true;
            while (showDetails) {
                String stringStatus;
                if (selectedTransfer.getStatusId() == 1) {
                    stringStatus = "Pending";
                } else if (selectedTransfer.getStatusId() == 2) {
                    stringStatus = "Approved";
                } else {
                    stringStatus = "Rejected";
                }
                String stringType;
                if (selectedTransfer.getTypeId() == 1) {
                    stringType = "Request";
                } else {
                    stringType = "Send";
                }
                System.out.println("-----------\nTransfer Details\n-----------");
                System.out.println("Id: " + selectedTransfer.getTransferId());
                System.out.println("From: " + consoleService.getUsernameFromAccount(selectedTransfer.getAccountFrom()));
                System.out.println("To: " + consoleService.getUsernameFromAccount(selectedTransfer.getAccountTo()));
                System.out.println("Type: " + stringType + "\nStatus: " + stringStatus + "\nAmount: " + selectedTransfer.getAmount());

                user.deleteAccountList();
                break;
            }


            user.deleteAccountList();
            break;


        }
    }

	private void viewPendingRequests() {



            User user = currentUser.getUser();
            consoleService.createUserAccountList(user);

            System.out.println("------------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID             To              Amount");
            System.out.println("------------------------------------------------");
            consoleService.createPendingTransferList(user);

            User userTo = null;

            for (TransferDto transfer : user.getTransferList()) {

                try {
                    userTo = restTemplate.getForObject(API_BASE_URL + "/users/transfers/to/" + transfer.getAccountTo(), User.class);
                } catch (RestClientResponseException | ResourceAccessException e) {
                    BasicLogger.log(e.getMessage());
                }
                System.out.println(transfer.getTransferId() + "           " + userTo.getUsername() + "         $" + transfer.getAmount());
            }

            TransferDto chosenTransfer = null;
            boolean chooseTransfer = true;
            while (chooseTransfer) {
                int transferChoice = 0;

                System.out.println("-------------------------------------------------");
                transferChoice = consoleService.promptForInt("Please enter transfer ID to Approve or Reject (0 to Cancel):");
                if (transferChoice == 0) {
                    mainMenu();
                }
                boolean transferOnList = false;
                while(!transferOnList) {
                    for (TransferDto transfer : user.getTransferList()) {
                        if (transfer.getTransferId() == transferChoice) {
                            chosenTransfer = transfer;
                            transferOnList = true;
                            break;
                        }
                    }
                    if (!transferOnList) {
                        System.out.println("Invalid transfer, number not on list");
                        break;
                    }

                }
                Transfer updatedTransfer = chosenTransfer.createTransfer();
                Account accountFrom = null;
                for (Account account : user.getAccountList()) {
                    if (updatedTransfer.getAccountFrom() == account.getAccountId()) {
                        accountFrom = account;
                        break;
                    }
                }
                consoleService.createUserAccountList(userTo);
                Account accountTo = null;
                for (Account account : userTo.getAccountList()) {
                    if (updatedTransfer.getAccountTo() == account.getAccountId()) {
                        accountTo = account;
                        break;
                    }
                }
                int decision = 0;
                decision = consoleService.promptForInt("Enter 1 to Approve or 2 to Reject" );
                if (decision == 1) {
                    updatedTransfer.setStatusId(2);
                    System.out.println("Transfer: " + updatedTransfer.getTransferId() + " approved!");
                    boolean success = accountFrom.sendMoney(accountTo, updatedTransfer.getAmount());
                    if (success) {
                        transferService.updateAccountBalance(accountTo);
                        transferService.updateAccountBalance(accountFrom);
                        transferService.updateTransfer(updatedTransfer);
                        break;
                    }

                } else if (decision == 2) {
                    updatedTransfer.setStatusId(3);
                    System.out.println("Transfer: " + updatedTransfer.getTransferId() + " rejected!");
                    transferService.updateTransfer(updatedTransfer);
                    break;
                } else {
                    System.out.println("Invalid selection, must select 1 or 2.");
                    transferService.updateTransfer(updatedTransfer);
                    break;
                }
            }




        user.deleteAccountList();
        user.deleteTransferList();
        userTo.deleteAccountList();
        userTo.deleteTransferList();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------


	private void sendBucks() {
        boolean sendBucks = true;
        while(sendBucks) {
            User user = currentUser.getUser();
            consoleService.createUserAccountList(user);
            viewCurrentBalance();
            int accountFromId = consoleService.promptForInt("From which account would you like to send money from? ");
            boolean onAccountList = false;
            Account fromAccount = null;
            for (Account account : user.getAccountList()) {
                if (account.getAccountId() == accountFromId) {
                    fromAccount = account;
                    onAccountList = true;
                    break;
                }
            }
            if (!onAccountList) {
                System.out.println("Please enter valid account number");
            }
            System.out.println("Who would you like to send money to? ");
            User[] users = null;
            try {
                users = restTemplate.getForObject(API_BASE_URL + "users", User[].class);
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
            consoleService.printUserList(users);
            int userId = consoleService.promptForInt("Enter the user's ID: ");
            boolean onUserList = false;
            User selectedUser = null;
            for (User userInSystem : users) {
                if (userId == currentUser.getUser().getId()) {
                    System.out.println("Cannot send money to yourself!");
                    break;
                }
                if (userInSystem.getId() == userId) {
                    onUserList = true;
                    selectedUser = userInSystem;
                    break;
                }
            }
            if (!onUserList) {
                System.out.println("Invalid ID");
                break;
            }
            consoleService.createUserAccountList(selectedUser);
            System.out.println(selectedUser.getUsername() + " Accounts:");
            for (Account account : selectedUser.getAccountList()) {
                System.out.println(account.getAccountId());
            }

            int accountToId = 0;
            Account toAccount = null;
            boolean choosingAccountTo = true;
            while(choosingAccountTo) {
                accountToId = consoleService.promptForInt("Which account do you want to send to? Press 0 to exit: ");
                if(accountToId == 0) {
                    sendBucks = false;
                    break;
                }
                boolean onToAccountList = false;
                for (Account account : selectedUser.getAccountList()) {
                    if (account.getAccountId() == accountToId) {
                        toAccount = account;
                        onToAccountList = true;
                        choosingAccountTo = false;
                        break;
                    }
                }
                if (!onToAccountList) {
                    System.out.println("Please enter valid account number:");
                }
            }

            if(!sendBucks) {
                break;
            }

            BigDecimal transferAmount = consoleService.promptForBigDecimal("How much do you want to send? ");
            Transfer transfer = new Transfer();
            transfer.setTypeId(2);
            transfer.setTransferAmount(transferAmount);
            transfer.setStatusId(2);
            transfer.setAccountFrom(accountFromId);
            transfer.setAccountTo(accountToId);

            boolean success = fromAccount.sendMoney(toAccount, transferAmount);
            if(success) {
                transferService.createTransfer(transfer);
                transferService.updateAccountBalance(toAccount);
                transferService.updateAccountBalance(fromAccount);
                sendBucks = false;
            } else {
                sendBucks = false;
            }

            user.deleteAccountList();
        }

	}

    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------


	private void requestBucks() {
        boolean requestBucks = true;
        while(requestBucks) {
            User user = currentUser.getUser();
            Account[] accounts = null;
            consoleService.createUserAccountList(user);
            viewCurrentBalance();
            int accountToId = consoleService.promptForInt("From which account would you like to request money to? ");
            boolean onAccountList = false;
            Account accountTo = null;
            for (Account account : user.getAccountList()) {
                if (account.getAccountId() == accountToId) {
                    accountTo = account;
                    onAccountList = true;
                    break;
                }
            }
            if (!onAccountList) {
                System.out.println("Please enter valid account number");
            }
            System.out.println("Who would you like to send money to? ");
            User[] users = null;
            try {
                users = restTemplate.getForObject(API_BASE_URL + "users", User[].class);
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
            consoleService.printUserList(users);
            int userId = consoleService.promptForInt("Enter the user's ID: ");
            boolean onUserList = false;
            User selectedUser = null;
            for (User userInSystem : users) {
                if (userId == currentUser.getUser().getId()) {
                    System.out.println("Cannot request money from yourself!");
                    break;
                }
                if (userInSystem.getId() == userId) {
                    onUserList = true;
                    selectedUser = userInSystem;
                    break;
                }
            }
            if (!onUserList) {
                System.out.println("Invalid ID");
                break;
            }
            Account[] toAccounts = null;
            try {
                toAccounts = restTemplate.getForObject(API_BASE_URL + "users/" + selectedUser.getId() + "/accounts", Account[].class);
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
            System.out.println(selectedUser.getUsername() + " Accounts:");
            for (Account account : toAccounts) {
                selectedUser.addToAccountList(account);
                System.out.println(account.getAccountId());
            }
            int accountFromId = consoleService.promptForInt("Which account do you want to request money from? ");
            Account accountFrom = null;
            boolean onToAccountList = false;
            for (Account account : toAccounts) {
                if (account.getAccountId() == accountFromId) {
                    accountFrom = account;
                    onToAccountList = true;
                    break;
                }
            }
            int sentRequest=0;
            if (!onAccountList) {
                System.out.println("Please enter valid account number");
            }
            BigDecimal transferAmount = consoleService.promptForBigDecimal("How much do you want to request for? ");
            Transfer transfer = new Transfer();
            transfer.setTypeId(1);
            transfer.setTransferAmount(transferAmount);
            transfer.setStatusId(1);
            transfer.setAccountFrom(accountFromId);
            transfer.setAccountTo(accountToId);

            transferService.createTransfer(transfer);
            requestBucks = false;


            user.deleteAccountList();
        }




    }

}
