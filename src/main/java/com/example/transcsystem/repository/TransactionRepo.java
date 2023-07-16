package com.example.transcsystem.repository;

import com.example.transcsystem.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transactions, String> {

    @Query(value = "SELECT * FROM tbl_transactions u WHERE u.account_no =:account_no LIMIT :lim ", nativeQuery = true)
    List<Transactions> findTopTransactionById(@Param("account_no") String account_no,
                                              @Param("lim") int lim);

    @Query(value = "SELECT * FROM tbl_transactions u WHERE u.transactionid =:id ", nativeQuery = true)
    Optional<Transactions> findTransactionById(@Param("id") String id);
}
