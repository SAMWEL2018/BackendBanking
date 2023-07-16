package com.example.transcsystem.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")

public class User {
    @Basic
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    @Basic
    @Column(name = "email",nullable = false,length = 50)
    private String email;

    @Id
    @Column(name = "id",nullable = false,length = 50,insertable = true,unique = true)
    @JsonProperty("id")
    private String id;

    @Basic
    @Column(name = "pin",nullable = false,length = 50)
    private String PIN;

    @Transient
    public String userAccount;

    public User() {
        this.PIN = Math.floor(Math.random()*(9999-1000+1)+1000)+"";
    }
}
