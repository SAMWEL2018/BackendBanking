package com.example.transcsystem;

import com.example.transcsystem.models.TransactionType;
import com.example.transcsystem.models.Transactions;
import com.example.transcsystem.models.User;
import com.example.transcsystem.repository.AccountRepo;
import com.example.transcsystem.repository.TransactionRepo;
import com.example.transcsystem.service.AccountsService;
import com.example.transcsystem.service.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class TranscSystemApplicationTests {

    @Autowired
    private Userservice userservice;
    @Autowired
    private AccountsService accountsService;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Test
    void contextLoads() {
    }
    @Test
    void addUser(){
        User user = User.builder()
                .email("sam@gmail.com")
                .name("sam")
                .id("1234")
                .PIN("12345")
        .build();

        Object addedUser = userservice.Registration(user);
    }

//    @Test
//    void addAccount(){
//        Accounts accounts = Accounts.builder()
//                .id("1")
//                .name("sam")
//                .balance("0")
//                .build();
//
//        Accounts ac =accountRepo.save(accounts);
//        System.out.println(accountRepo.findById("1").orElse(new Accounts()).getName());
//    }

    @Test
    void transact(){

        addUser();
        log.info( "Return Valued : {}", accountRepo.findCreditById("1").orElse(0.0));

        log.info("The Account Balance {}", accountsService.balance("1"));

//        Transactions transactions= Transactions.builder()
//                .TransactionID("1")
//                .name("sam")
//                .Transactiontype(TransactionType.DEPOSIT.toString())
//                .amount("100")
//                .build();
//
//        Transactions tr = transactionRepo.save(transactions);
//        System.out.printf(transactionRepo.findById("1").orElse(new Transactions()).getAmount());

    }

    @Test
    void fetchTest(){
        Transactions transactions = Transactions
                .builder()
                .name("Jose")
                .transactionID("12")
                .accountId("1234")
                .transactionType(TransactionType.DEPOSIT.name())
                .amount("1000.0")
                .build();
        transactionRepo.save(transactions);
        System.out.println(transactionRepo.findTopTransactionById("1234", 10).size());
    }


}
