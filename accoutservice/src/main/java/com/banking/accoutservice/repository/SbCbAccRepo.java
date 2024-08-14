package com.banking.accoutservice.repository;

import com.banking.accoutservice.entities.SbCbdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SbCbAccRepo extends JpaRepository<SbCbdetail, Long> {

    List<SbCbdetail> findAllByClientID(Long clientID);

    SbCbdetail findAllByAccountID(Long accountID);
}
