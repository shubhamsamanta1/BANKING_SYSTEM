package com.banking.clientservice.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccDetail {


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

    Date tmStamp;
}
