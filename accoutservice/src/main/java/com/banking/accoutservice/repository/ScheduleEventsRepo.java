package com.banking.accoutservice.repository;

import com.banking.accoutservice.entities.ScheduleEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleEventsRepo extends JpaRepository<ScheduleEvents, Long> {

    List<ScheduleEvents> findAllByAccountID(Long accountID);

    @Modifying
    @Query(value = "UPDATE schedule_event_details u SET" +
            "u.txn_amount = :txn_amount," +
            "u.tm_stamp = :tm_stamp WHERE u.accountid = :accountid", nativeQuery = true)
    void updateScheduleEvent(@Param("txn_amount") BigDecimal txn_amount,
                             @Param("tm_stamp") LocalDateTime tm_stamp,
                             @Param("accountid") Long accountid);
}
