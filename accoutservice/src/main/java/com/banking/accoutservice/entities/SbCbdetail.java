package com.banking.accoutservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.processing.Generated;
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

    Long accBalance;

    LocalDate lastTxnDate ;

    String accStatus;

    Long lastDoneTxnID;

    @UpdateTimestamp
    Date tmStamp;



}
