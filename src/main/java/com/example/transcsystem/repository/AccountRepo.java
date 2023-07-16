package com.example.transcsystem.repository;

import com.example.transcsystem.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Accounts,String> {

    @Query(value = "SELECT SUM(credit) FROM tbl_accounts WHERE id =:id", nativeQuery = true)
    Optional<Double> findCreditById(@Param("id") String id);

    @Query(value = "SELECT SUM(debit) FROM tbl_accounts WHERE id =:id", nativeQuery = true)
    Optional<Double>  findDebitById(@Param("id") String id);

    @Query(value = "SELECT * FROM tbl_accounts WHERE id =:id", nativeQuery = true)
    Optional<Accounts>  findCustomerBalanceById(@Param("id") String id);

}
