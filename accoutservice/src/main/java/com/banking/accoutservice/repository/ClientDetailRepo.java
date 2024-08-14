package com.banking.accoutservice.repository;

import com.banking.accoutservice.entities.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailRepo extends JpaRepository<ClientDetail, Long> {


    @Query(value = "SELECT email FROM client_details u WHERE u.clientid = :id", nativeQuery = true)
    String findEmailByClientID(@Param("id") Long id);

    @Query(value = "SELECT contact FROM client_details u WHERE u.clientid = :id", nativeQuery = true)
    Long findContactByClientID(@Param("id") Long id);


}
