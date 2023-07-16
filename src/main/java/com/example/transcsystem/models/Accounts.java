package com.example.transcsystem.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_accounts")

public class Accounts {

    @Id
    @Column(name = "id",nullable = false,length = 50)
    private String id;

    @Basic
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    @Basic
    @Column(name = "debit",nullable = false,length = 50)
    private double debit;

    @Basic
    @Column(name = "credit",nullable = false,length = 50)
    private double credit;

    @Basic
    @Column(name = "balance",nullable = false,length = 50)
    private double balance;

    @Basic
    @Column(name = "user_id",nullable = false,length = 50)
    private String userID;

    @Basic
    @Column(name = "transaction_id",nullable = false,length = 50)
    private double transactionId;


}
