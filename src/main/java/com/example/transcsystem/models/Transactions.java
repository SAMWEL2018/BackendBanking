package com.example.transcsystem.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "tbl_transactions")
public class Transactions {

    @Id
    @Column(name = "transactionid",nullable = false,length = 50,unique = true)
    private  String transactionID;

    @Basic
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    @Basic
    @Column(name = "transactiontype",nullable = false,length = 50)
    private String transactionType;

    @Basic
    @Column(name = "amount",nullable = false,length = 50)
    private String amount;

    @Basic
    @Column (name = "account_no", nullable = false, length = 20)
    private String accountId;

    @Basic
    @Column (name = "beneficiary_account", nullable = true, length = 20)
    private String beneficiaryAccount;

    @Basic
    @Column (name = "transaction_time", nullable = true, length = 20)
    @CreationTimestamp
    private Timestamp timeCreated;


}
