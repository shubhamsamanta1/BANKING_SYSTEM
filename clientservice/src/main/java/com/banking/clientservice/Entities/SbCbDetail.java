package com.banking.clientservice.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SbCbDetail {

    Long accountID;

    Long clientID;

    Long branchCode;

    String accType;

    Long accBalance;

    LocalDate lastTxnDate ;

    String accStatus;

    Long lastDoneTxnID;

    Date tmStamp;



}
