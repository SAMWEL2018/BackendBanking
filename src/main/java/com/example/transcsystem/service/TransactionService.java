package com.example.transcsystem.service;


import com.example.transcsystem.models.Accounts;
import com.example.transcsystem.models.ProcessingResp;
import com.example.transcsystem.models.TransactionType;
import com.example.transcsystem.models.Transactions;
import com.example.transcsystem.repository.AccountRepo;
import com.example.transcsystem.repository.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountsService accountsService;

    public List<Transactions> getTransactions(int transactionLimit,String accountId){
        return  transactionRepo.findTopTransactionById(accountId , transactionLimit);
    }

    public Transactions getSingleTransaction(String id){
        return transactionRepo.findTransactionById(id).orElse(null);
    }

    public ProcessingResp ProcessTransaction(Transactions transactions, TransactionType transactionType) {


        return switch (transactionType) {
            case DEPOSIT, REVERSAL -> processDeposit(transactions);
            case WITHDRAW -> processWithdraw(transactions);
            case TRANSFER -> transferFunds(transactions);
        };
    }

    public ProcessingResp processDeposit(Transactions transactions) {
        ProcessingResp resp = null;
        Optional<Accounts> account = accountRepo.findById(transactions.getAccountId());
        if (account.isPresent()) {
            double amount = Double.parseDouble(transactions.getAmount());
            if (amount > 0) {
                depositAccount(account.get(), amount);
                /*Generate new Transaction ID*/
                String id = LocalDateTime.now(ZoneOffset.UTC).getNano() + "";
                log.info("Transaction ID : {}", id);
                transactions.setTransactionID(id);
                transactions.setTransactionType(TransactionType.DEPOSIT.name());
                transactionRepo.save(transactions);
                resp = ProcessingResp.builder()
                        .status("SUCCESS")
                        .responseMessage("successful")
                        .responseCode("000")
                        .transaction(transactions)
                        .balance(accountsService.balance(transactions.getAccountId()))
                        .build();
            } else {
                resp = ProcessingResp.builder()
                        .status("FAILED")
                        .responseCode("001")
                        .responseMessage("Invalid amount")
                        .build();
            }
        } else {
            resp = ProcessingResp.builder()
                    .status("FAILED")
                    .responseCode("002")
                    .responseMessage("Account Not Found")
                    .build();
        }

        return resp;
    }

    private void depositAccount(Accounts acct, double amount) {

        acct.setCredit(acct.getCredit() + amount);
        acct.setBalance(acct.getCredit() - acct.getDebit());
        accountRepo.save(acct);
    }

    public ProcessingResp processWithdraw(Transactions transactions) {

        ProcessingResp resp = null;

        Optional<Accounts> account = accountRepo.findById(transactions.getAccountId());
        if (account.isPresent()) {

            double amount = Double.parseDouble(transactions.getAmount());


            String id = LocalDateTime.now(ZoneOffset.UTC).getNano() + "";
            log.info("Transaction ID : {}", id);
            Accounts acct = account.get();
            double bal = acct.getBalance();

            if (amount < bal) {
                withdrawAccount(amount, acct);

                transactions.setTransactionID(id);
                transactions.setTransactionType(TransactionType.WITHDRAW.name());
                transactionRepo.save(transactions);
                resp = ProcessingResp.builder()
                        .status("SUCCESS")
                        .responseCode("000")
                        .responseMessage("SUCCESSFUL")
                        .transaction(transactions)
                        .balance(accountsService.balance(transactions.getAccountId()))
                        .build();
            } else {
                resp = ProcessingResp.builder()
                        .status("FAILED")
                        .responseCode("004")
                        .responseMessage("Insufficient Funds")
                        .build();
            }

        } else {
            resp = ProcessingResp.builder()
                    .status("FAILED")
                    .responseCode("002")
                    .responseMessage("Account Not Found")
                    .build();
        }

        return resp;
    }

    private void withdrawAccount(double amount, Accounts acct) {
        acct.setDebit(acct.getDebit() + amount);
        acct.setBalance(acct.getCredit() - acct.getDebit());
        accountRepo.save(acct);
    }

    public ProcessingResp transferFunds(Transactions transactions) {
        ProcessingResp resp = null;

        Optional<Accounts> accounts = accountRepo.findById(transactions.getAccountId());
        Optional<Accounts> accounts1 = accountRepo.findById(transactions.getBeneficiaryAccount());

        if (accounts.isPresent() && accounts1.isPresent()) {

            double amount = Double.parseDouble(transactions.getAmount());

            if (amount > 0) {
                if (accounts.get().getBalance() > amount) {

                    withdrawAccount(amount, accounts.get());

                    String id = LocalDateTime.now(ZoneOffset.UTC).getNano() + "";
                    log.info("Transaction ID : {}", id);

                    transactions.setTransactionID(id);
                    transactions.setTransactionType(TransactionType.TRANSFER.name());
                    transactionRepo.save(transactions);

                    depositAccount(accounts1.get(), amount);
                    resp = ProcessingResp.builder()
                            .status("SUCCESS")
                            .responseCode("000")
                            .responseMessage("SUCCESSFUL")
                            .transaction(transactions)
                            .build();

                } else {
                    resp = ProcessingResp.builder()
                            .status("FAILED")
                            .responseCode("004")
                            .responseMessage("Insufficient Funds")
                            .build();
                }
            } else {
                resp = ProcessingResp.builder()
                        .status("FAILED")
                        .responseCode("002")
                        .responseMessage("Invalid Amount")
                        .build();

            }
        } else {
            resp = ProcessingResp.builder()
                    .status("FAILED")
                    .responseCode("006")
                    .responseMessage("Account Not Found")
                    .build();
        }
        return resp;
    }

}
