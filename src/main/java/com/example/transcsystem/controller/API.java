package com.example.transcsystem.controller;

import com.example.transcsystem.models.*;
import com.example.transcsystem.security.JwtTokenService;
import com.example.transcsystem.service.AccountsService;
import com.example.transcsystem.service.TransactionService;
import com.example.transcsystem.service.Userservice;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1")
public class API {

    @Autowired
    private Userservice service;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountsService accountsService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Object> Registration(@RequestBody User user) {

        return ResponseEntity.ok(service.Registration(user));
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public User getUser(@Param("id") String id) {
        return service.getUserUsingId(id);
    }

    @RequestMapping(value = "/getTransaction/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getTransaction(@PathVariable("id") String id) {
        Transactions t = transactionService.getSingleTransaction(id);
        return t == null ? ResponseEntity.status(404).body(ProcessingResp.builder().responseMessage("Not found").build())
                : ResponseEntity.ok(t);
    }

    @RequestMapping(value = "/getAccount", method = RequestMethod.GET)
    public Accounts getAccount(@Param("id") String id) {
        return service.getAccountUsingId(id);
    }

    @RequestMapping(value = "/getCustomerBalance/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCustomerBalance(@PathVariable("id")String id) {
        return ResponseEntity.ok(accountsService.actBalance(id));
    }

    @RequestMapping(value = "/get-mini-statement/{account_id}", method = RequestMethod.GET)
    public List<Transactions> getStatement(@PathVariable("account_id") String id) {
        return transactionService.getTransactions(10,id);
    }

//    @CrossOrigin(
//            origins = "http://localhost:4200",
//            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
//            "Access-Control-Request-Headers","Authorization"},allowCredentials = "true"
//    )
    @CrossOrigin
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<Object> CreditAccount(@RequestBody Optional<Transactions> tsn) {
        if (tsn.isPresent()) {
            try {
                return ResponseEntity.ok(transactionService.ProcessTransaction(tsn.get(), TransactionType.DEPOSIT));
            } catch (Exception e) {
                log.error("Error In deposit: {}", e.getMessage());
                return ResponseEntity.status(500).body(
                        ProcessingResp.builder()
                                .status("FAILED")
                                .responseMessage("Internal server error")
                                .responseCode("003")
                                .build()
                );
            }
        } else {
            return ResponseEntity.badRequest().body(ProcessingResp.builder()
                    .status("FAILED")
                    .responseMessage("Internal server error")
                    .responseCode("003")
                    .build()
            );
        }
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<ProcessingResp> Withdraw(@RequestBody Optional<Transactions> tsn) {
        if (tsn.isPresent()) {
            try {
                return ResponseEntity.ok(transactionService.ProcessTransaction(tsn.get(), TransactionType.WITHDRAW));
            } catch (Exception e) {
                log.error("Error In WITHDRAWAL: {}", e.getMessage());
                return ResponseEntity.status(500).body(
                        ProcessingResp.builder()
                                .status("FAILED")
                                .responseMessage("Internal server error")
                                .responseCode("003")
                                .build()
                );
            }
        } else {
            return ResponseEntity.badRequest().body(ProcessingResp.builder()
                    .status("FAILED")
                    .responseMessage("Internal server error")
                    .responseCode("003")
                    .build()
            );
        }
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<ProcessingResp> transferFunds(@RequestBody Optional<Transactions> tsn) {
        if (tsn.isPresent()) {

            try {
                return ResponseEntity.ok(transactionService.ProcessTransaction(tsn.get(), TransactionType.TRANSFER));
            } catch (Exception e) {
                log.error("Error In Transfer: {}", e.getMessage());
                return ResponseEntity.status(500).body(
                        ProcessingResp.builder()
                                .status("FAILED")
                                .responseMessage("Internal server error")
                                .responseCode("003")
                                .build()
                );
            }

        } else {
            return ResponseEntity.badRequest().body(ProcessingResp.builder()
                    .status("FAILED")
                    .responseMessage("Internal server error")
                    .responseCode("003")
                    .build()
            );
        }
    }

}
