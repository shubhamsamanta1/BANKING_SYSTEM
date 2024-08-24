package com.banking.transactionservice.repository;

import com.banking.transactionservice.entities.LoanAccDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface LoanAccRepo extends JpaRepository<LoanAccDetail, Long> {

    LoanAccDetail findAllByAccountID(Long accountID);

    @Modifying
    @Query(value = "UPDATE loan_acc_details u SET u.loan_amount = :loan_amount," +
            "u.amount_to_be_paid = :amount_to_be_paid," +
            "u.loan_amount = :loan_amount," +
            "u.emi_amount = :emi_amount," +
            "u.amount_paid_till_date = :amount_paid_till_date," +
            "u.last_done_txnid = :last_done_txnid," +
            "u.acc_status = :acc_status," +
            "u.emi_cycles_completed = :emi_cycles_completed," +
            "u.tm_stamp = :tm_stamp WHERE u.accountid = :accountid", nativeQuery = true)
    void updateLoanAccount(@Param("loan_amount") BigDecimal loan_amount,
                           @Param("amount_to_be_paid") BigDecimal amount_to_be_paid,
                           @Param("emi_amount") BigDecimal emi_amount,
                           @Param("amount_paid_till_date") BigDecimal amount_paid_till_date,
                           @Param("last_done_txnid") Long last_done_txnid,
                           @Param("acc_status") String acc_status,
                           @Param("emi_cycles_completed") int emi_cycles_completed,
                           @Param("tm_stamp") LocalDateTime tm_stamp,
                           @Param("accountid") Long accountid);

}
