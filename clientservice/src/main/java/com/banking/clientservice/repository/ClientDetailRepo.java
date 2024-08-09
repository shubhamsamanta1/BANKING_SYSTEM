package com.banking.clientservice.repository;

import com.banking.clientservice.Entities.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailRepo extends JpaRepository<ClientDetail, Long> {

}
