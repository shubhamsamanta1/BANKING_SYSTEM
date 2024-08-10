package com.banking.noticationservice.repository;

import com.banking.noticationservice.entities.NotifyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyDetailRepo extends JpaRepository<NotifyDetail, Long> {

    List<NotifyDetail> findAllByaccountId(Long accountId);

    List<NotifyDetail> findAllByclientId(Long clientId);
}
