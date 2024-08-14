package com.banking.accoutservice.repository;

import com.banking.accoutservice.entities.ScheduleEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleEventsRepo extends JpaRepository<ScheduleEvents, Long> {

    List<ScheduleEvents> findAllByAccountID(Long accountID);
}
