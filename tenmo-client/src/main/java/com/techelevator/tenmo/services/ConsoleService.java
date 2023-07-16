package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printAccountBalances(Account[] accounts) {
        System.out.println("--------------------------------------------");
        System.out.println("Accounts                   Balance");
        System.out.println("--------------------------------------------");
        for (Account account : accounts) {
            BigDecimal currentBalance = restTemplate.getForObject(API_BASE_URL + "/accounts/" + account.getAccountId() + "/balance", BigDecimal.class);

            System.out.println(account.getAccountId() + ":                       $" + currentBalance);
        }
    }

    public void printTransferScreen(Account account, TransferDto[] transfers) {
        System.out.println("-----------------------TRANSFERS----------------------------");
        System.out.println("------------------------------------------------------------");
        System.out.println("ID              From/To                  Amount");
        System.out.println("------------------------------------------------------------");
        for(TransferDto transfer : transfers) {
            if(account.getAccountId() == transfer.getAccountTo()) {
                User userFrom = null;
                try {
                    userFrom = restTemplate.getForObject(API_BASE_URL + "/users/transfers/from/" + transfer.getAccountFrom(), User.class);
                }  catch (RestClientResponseException | ResourceAccessException e) {
                    BasicLogger.log(e.getMessage());
                }
                System.out.println(transfer.getTransferId() + "              " + "From: " + userFrom.getUsername() + "                   $" + transfer.getAmount());
            } else {
                User userTo = null;
                try {
                    userTo = restTemplate.getForObject(API_BASE_URL + "/users/transfers/to/" + transfer.getAccountTo(), User.class);
                }  catch (RestClientResponseException | ResourceAccessException e) {
                    BasicLogger.log(e.getMessage());
                }
                System.out.println(transfer.getTransferId() + "              " + "To: " + userTo.getUsername() + "                     $"  + transfer.getAmount());
            }

        }
    }

    public void printUserList(User[] users) {
        System.out.println("--------------------------------------------");
        System.out.println("Users ID         Username");
        System.out.println("--------------------------------------------");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + "         Username: " + user.getUsername());
        }
    }

    public void createUserAccountList(User user) {
        Account[] accounts = null;
        try {
            accounts = restTemplate.getForObject(API_BASE_URL + "users/" + user.getId() + "/accounts", Account[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        for (Account account : accounts) {
            user.addToAccountList(account);
        }
    }

    public void createPendingTransferList(User user) {
        TransferDto[] transfers = null;
        try {
            transfers = restTemplate.getForObject(API_BASE_URL + "users/" + user.getId() + "/pendingtransfers", TransferDto[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        for (TransferDto transfer : transfers) {
            user.addToTransferList(transfer);
        }
    }

    public String getUsernameFromAccount(int accountId) {
        User user = new User();
        try {
            user = restTemplate.getForObject(API_BASE_URL + "/users/transfers/to/" + accountId, User.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return user.getUsername();
    }








}
