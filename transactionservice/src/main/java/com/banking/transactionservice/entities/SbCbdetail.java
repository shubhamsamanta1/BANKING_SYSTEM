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
@Table(name = "SbCb_Details")
public class SbCbdetail {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long accountID;

    Long clientID;

    Long branchCode;

    String accType;

    BigDecimal accBalance;

    LocalDate lastTxnDate ;

    String accStatus;

    Long lastDoneTxnID;

    @UpdateTimestamp
    Date tmStamp;



}
