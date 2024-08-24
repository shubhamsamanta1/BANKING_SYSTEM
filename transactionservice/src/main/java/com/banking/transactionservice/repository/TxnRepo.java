package com.banking.transactionservice.repository;

import com.banking.transactionservice.entities.TxnDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TxnRepo extends JpaRepository<TxnDetails, Long> {

    List<TxnDetails> findAllByAccountID(Long accountID);
}
