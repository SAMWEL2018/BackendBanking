package com.example.transcsystem.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class repository {

    private final JdbcTemplate jdbcTemplate;

    public repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



//
//    public int register(String name,String email,String ID){
//        String sql = "INSERT INTO Customers VALUES('"+name+"','"+email+"','"+ID+"')";
//        return jdbcTemplate.update(sql);
//    }
}
