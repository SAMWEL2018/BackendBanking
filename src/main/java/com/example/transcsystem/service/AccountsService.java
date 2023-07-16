package com.example.transcsystem.service;

import com.example.transcsystem.models.Accounts;
import com.example.transcsystem.models.ProcessingResp;
import com.example.transcsystem.repository.AccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AccountsService {

    @Autowired
    private AccountRepo accountRepo;

    public double credit(String amount, String AccountId) {
        return 0;
    }

    public double debit(String amount, String AccountId) {
        return 0;
    }

    public double balance(String AccountId) {

        double credit = getCredit(AccountId);
        double debit = getDebit(AccountId);

        return credit - debit;
    }

    public Object actBalance(String AccountId){

       Optional<Accounts> accounts =  accountRepo.findById(AccountId);
        if (accounts.isPresent()) {
            return new HashMap<>(){{
                put("status", "SUCCESS");
                put("account", AccountId);
                put("balance", accounts.get().getBalance());
            }};
        }else {
            return ProcessingResp.builder()
                    .status("FAILED")
                    .responseCode("007")
                    .responseMessage("Account Not Found")
                    .build();
        }
    }

    public Double getDebit(String AccountId) {
        return accountRepo.findDebitById(AccountId).orElse(0.0);
    }

    public double getCredit(String AccountId) {
        return accountRepo.findCreditById(AccountId).orElse(0.0);
    }


}
