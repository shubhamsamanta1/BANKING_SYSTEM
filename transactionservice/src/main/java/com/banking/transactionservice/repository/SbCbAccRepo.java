package com.banking.transactionservice.repository;

import com.banking.transactionservice.entities.SbCbdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface SbCbAccRepo extends JpaRepository<SbCbdetail, Long> {

    SbCbdetail findAllByAccountID(Long accountID);

    @Modifying
    @Query(value = "UPDATE Sb_Cb_Details u SET u.acc_balance = :balance, u.last_done_txnid = :txnid, u.last_txn_date = :txndate, " +
            " u.tm_stamp = :tmstamp WHERE u.accountid = :accountid", nativeQuery = true)
    void updateSbCbAccount(@Param("balance") BigDecimal balance, @Param("txnid") Long txnid,
                           @Param("txndate") LocalDate txndate, @Param("tmstamp") LocalDateTime tmstamp ,
                           @Param("accountid") Long accountid );


}
