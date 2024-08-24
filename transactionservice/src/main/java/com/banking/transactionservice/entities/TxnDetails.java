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
@Table(name = "Transaction_Detail")
public class TxnDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long txnID;

    Long accountID;

    Long payingAccID;

    String txnType;

    LocalDate txnDate;

    BigDecimal txnAmount;

    String txnStatus;

    String txnReason;

    String txnMessage;

    @UpdateTimestamp
    Date tmStamp;

}
