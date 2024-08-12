package com.banking.loginservice.repository;

import com.banking.loginservice.entites.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientDetail, Long> {

    List<ClientDetail> findByclientID(Long ID);

    List<ClientDetail> findByPassword(String password);

    @Modifying
    @Query(value = "UPDATE client_details u SET u.password = :password, u.tm_stamp = :tmstamp WHERE u.clientid = :id", nativeQuery = true)
    void updatePassword(@Param("id") Long id, @Param("password") String password, @Param("tmstamp") LocalDateTime tmstamp);

    @Query(value = "SELECT email FROM client_details u WHERE u.clientid = :id", nativeQuery = true)
    String findByEmail(@Param("id") Long id);

    @Query(value = "SELECT contact FROM client_details u WHERE u.clientid = :id", nativeQuery = true)
    Long findByContact(@Param("id") Long id);

    @Query(value = "SELECT * FROM client_details u WHERE u.gov_id = :gov_id", nativeQuery = true)
    ClientDetail findByGovId(@Param("gov_id") String gov_id);
}
