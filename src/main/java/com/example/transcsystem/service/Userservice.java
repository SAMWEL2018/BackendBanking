package com.example.transcsystem.service;

import com.example.transcsystem.models.Accounts;

import com.example.transcsystem.models.ProcessingResp;
import com.example.transcsystem.models.Transactions;
import com.example.transcsystem.models.User;
import com.example.transcsystem.repository.AccountRepo;
import com.example.transcsystem.repository.TransactionRepo;
import com.example.transcsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Userservice {

    @Autowired
    private UserRepo repo;

    @Autowired
    private AccountRepo AccRepo;

    @Autowired
    private TransactionRepo transact;


    @Transactional
    public Object Registration(User user) {
        user.setPIN(((int) Math.floor(Math.random() * (9999 - 1000 + 1) + 1000)) + "");

        Accounts accounts = new Accounts();
        accounts.setId(((int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000)) + "");
        accounts.setUserID(user.getId());
        accounts.setName(user.getName());
        accounts.setDebit(0);
        accounts.setCredit(0);
        accounts.setBalance(0);

        AccRepo.save(accounts);
        user.setUserAccount(accounts.getId());
        repo.save(user);

        return ProcessingResp.builder()
                .status("SUCCESS")
                .responseMessage("successful")
                .responseCode("000")
                .user(user)
                .build();

    }

    public User getUserUsingId(String id) {
        return repo.findById(id).orElse(null);
    }


    public Accounts getAccountUsingId(String id) {
        return AccRepo.findById(id).orElse(new Accounts());
    }

    public Transactions CreditAccount(Transactions transactions) {

        return transact.save(transactions);
    }

}
