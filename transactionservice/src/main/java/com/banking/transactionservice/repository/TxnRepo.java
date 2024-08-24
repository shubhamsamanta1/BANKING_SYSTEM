package com.banking.transactionservice.repository;

import com.banking.transactionservice.entities.TxnDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TxnRepo extends JpaRepository<TxnDetails, Long> {

    @Query(value = "SELECT * FROM transaction_detail u WHERE u.txn_type IN ('LOAN') AND u.accountid = :accountid", nativeQuery = true)
    List<TxnDetails> getLoanTxn(Long accountid);

    @Query(value = "SELECT * FROM transaction_detail u WHERE u.txn_type IN ('CREDIT','DEBIT') AND u.accountid = :accountid", nativeQuery = true)
    List<TxnDetails> getSbCbTxn(Long accountid);
}
