package com.example.BankingApp.Service;

import com.example.BankingApp.Dto.TransferRequestDto;
import com.example.BankingApp.Repository.AccountDetailsRepository;
import com.example.BankingApp.Repository.TransactionRepository;
import com.example.BankingApp.model.AccountDetails;
import com.example.BankingApp.model.Transaction;
import com.example.BankingApp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankingService {

    private static final Logger logger = LoggerFactory.getLogger(BankingService.class);

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void deposit(Float amount) {

        String userEmail = getCurrentUser();

        System.out.println("INSIDE DEPOSIT SERVICE. JAVA: " + userEmail+ " , " + amount);
        logger.info("Starting deposit for user: {}", userEmail);
        AccountDetails accountDetails = accountDetailsRepository.findById(userEmail)
                .orElseThrow(() -> {
                    logger.error("Account not found for user: {}", userEmail);
                    return new RuntimeException("Account not found");
                });
        logger.info("Current balance for user {}: {}", userEmail, accountDetails.getBalance());

        accountDetails.setBalance(accountDetails.getBalance()+amount);
        accountDetailsRepository.save(accountDetails);
        logger.info("Updated balance for user {}: {}", userEmail, accountDetails.getBalance());

        saveTransaction(amount, "DEPOSIT", "SELF");
        //logger.info("Transaction recorded for user {}: {}", userEmail, transaction);
    }

    public String transfer(TransferRequestDto transferRequestDto) {
        String userEmail = getCurrentUser();

        AccountDetails sourceAccount = accountDetailsRepository.findById(userEmail).orElseThrow(() -> {
            logger.info("Source Account not found!!");
            new RuntimeException("Source account not found");
            return null;
        });
        AccountDetails destinationAccount = accountDetailsRepository.findById(transferRequestDto.getDestinationAccountId()).orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (sourceAccount.getBalance() < transferRequestDto.getAmount()) {
            return("Insufficient balance in source account");
        }

        Float amount = transferRequestDto.getAmount();
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountDetailsRepository.save(sourceAccount);
        accountDetailsRepository.save(destinationAccount);
        saveTransaction(amount, "transfer", destinationAccount.getEmail());
        return "Transfer successful";
    }

    public Float balance() {
        String userEmail = getCurrentUser();

        AccountDetails sourceAccount = accountDetailsRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("Source account not found"));
        return sourceAccount.getBalance();

    }

    public String withdraw(Float amount) {
        Float balance = balance();

        if(amount > balance) {
            return "Insufficient Funds!";
        } else {
            String userEmail = getCurrentUser();
            AccountDetails account = accountDetailsRepository.findById(userEmail).orElseThrow(() -> {
                logger.info("Source Account not found!!");
                new RuntimeException("Source account not found");
                return null;
            });
            account.setBalance(account.getBalance() - amount);
            accountDetailsRepository.save(account);
            saveTransaction(amount, "withdraw", "SELF");
            return "OK";
        }
    }

    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();
        return userEmail;

    }


    /*
    Saves a transaction to transaction table in db.
    @Param String amount: set an amount
    @Param String transaction_type: withdraw / deposit / transfer
    @Param String transaction_to: set to SELF while making withdraw or deposit to your account or else mention target email id when making an amount transfer

     */
    public void saveTransaction( Float amount, String transaction_type, String transaction_to) {
        String userEmail = getCurrentUser();
        Transaction transaction = new Transaction();
        UUID id = UUID.randomUUID();
        transaction.setTransactionId(id);
        transaction.setEmail(userEmail);
        transaction_type.toUpperCase();
        transaction.setTransactionType(transaction_type);
        transaction.setAmount(amount);

        transaction.setTransactionTo(transaction_to);


        System.out.println("Transaction ID: "+ id+", EMAIL: "+userEmail+", Amount = "+amount+", Type = Deposit");

        transaction.setTransactionDate(Instant.now());

        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            System.out.println("Exception " +e);
        }
    }


    public List<Transaction> getStatement() {
        String userEmail = getCurrentUser();
        List<Transaction> statements = transactionRepository.getStatementForUser(userEmail);
        return statements;
    }


}
