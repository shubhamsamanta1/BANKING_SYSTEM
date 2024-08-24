package com.banking.accoutservice.entities;

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
@Table(name = "LoanAcc_Details")
public class LoanAccDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long accountID;

    Long clientID;

    Long branchCode;

    String paymentSourceBankName;

    Long paymentSourceBankID;

    BigDecimal intrestRate;

    BigDecimal loanAmount;

    BigDecimal AmountToBePaid;

    BigDecimal amountPaidTillDate;

    int tenure;

    int emiCyclesCompleted;

    BigDecimal emiAmount;

    String loanType;

    LocalDate emiCycleDate;

    Long lastDoneTxnID;

    String accStatus;

    @UpdateTimestamp
    Date tmStamp;

}
