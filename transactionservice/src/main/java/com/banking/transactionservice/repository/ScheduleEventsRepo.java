package com.banking.transactionservice.repository;

import com.banking.transactionservice.entities.LoanAccDetail;
import com.banking.transactionservice.entities.ScheduleEvents;
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

    ScheduleEvents findAllByAccountID(Long accountID);

    @Modifying
    @Query(value = "UPDATE schedule_event_details u SET u.last_txn_date = :last_txn_date," +
            "u.txn_amount = :txn_amount," +
            "u.event_status = :event_status," +
            "u.tm_stamp = :tm_stamp WHERE u.accountid = :accountid", nativeQuery = true)
    void updateScheduleEvent(@Param("last_txn_date") LocalDate last_txn_date,
                           @Param("txn_amount") BigDecimal txn_amount,
                           @Param("event_status") String event_status,
                           @Param("tm_stamp") LocalDateTime tm_stamp,
                           @Param("accountid") Long accountid);

    @Query(value = "SELECT * FROM schedule_event_details u WHERE u.next_txn_date = :next_txn_date", nativeQuery = true)
    List<ScheduleEvents> getEventsByDate(@Param("next_txn_date") LocalDate localDate);

    @Modifying
    @Query(value = "UPDATE schedule_event_details u SET u.next_txn_date = :next_txn_date," +
            "u.tm_stamp = :tm_stamp WHERE u.eventid = :eventid", nativeQuery = true)
    void updateScheduleEventByAutoTxn(@Param("next_txn_date") LocalDate next_txn_date,
                             @Param("tm_stamp") LocalDateTime tm_stamp,
                             @Param("eventid") Long eventid);
}
