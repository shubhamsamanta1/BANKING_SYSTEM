package com.banking.loginservice.repository;

import com.banking.loginservice.entites.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientDetail, Long> {

    List<ClientDetail> findByclientID(Long ID);

    List<ClientDetail> findByPassword(String password);

    @Modifying
    @Query(value = "UPDATE client_details u SET u.password = :password, u.tm_stamp = :tmstamp WHERE u.clientid = :id", nativeQuery = true)
    void updatePassword(@Param("id") Long id, @Param("password") String password, @Param("tmstamp") LocalDateTime tmstamp);
}
