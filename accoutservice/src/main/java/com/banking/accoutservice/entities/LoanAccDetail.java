package com.banking.accoutservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

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

    int intrestRate;

    Long loanAmount;

    Long amountPaidTillDate;

    Long balanceAmountToBePaid;

    int tenureInMonths;

    int emiCyclesCompleted;

    Long emiAmount;

    String loanType;

    LocalDate emiCycleDate;

    Long lastDoneTxnID;

    String accStatus;

    @UpdateTimestamp
    Date tmStamp;

}
