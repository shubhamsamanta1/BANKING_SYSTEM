package com.banking.accoutservice.repository;

import com.banking.accoutservice.entities.LoanAccDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanAccRepo extends JpaRepository<LoanAccDetail, Long> {

    List<LoanAccDetail> findAllByClientID(Long clientID);

    LoanAccDetail findAllByAccountID(Long accountID);
}
