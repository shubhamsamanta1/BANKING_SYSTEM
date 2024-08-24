package com.banking.transactionservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ScheduleEvent_Details")
public class ScheduleEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long eventID;

    Long accountID;

    Long payerAccountID;

    String txnType;

    Long clientID;

    LocalDate eventStartDate;

    LocalDate eventEndDate;

    LocalDate lastTxnDate;

    LocalDate nextTxnDate;

    String eventStatus;

    BigDecimal txnAmount ;

    String eventReason ;

    @UpdateTimestamp
    Date tmStamp;
}
