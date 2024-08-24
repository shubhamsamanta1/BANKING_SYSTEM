package com.banking.transactionservice.sevice;

import com.banking.transactionservice.entities.*;
import com.banking.transactionservice.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    SbCbAccRepo sbCbAccRepo;

    @Autowired
    LoanAccRepo loanAccRepo;

    @Autowired
    TxnRepo txnRepo;

    @Autowired
    ScheduleEventsRepo scheduleEventsRepo;

    @Autowired
    ClientDetailRepo clientDetailRepo;

    @Autowired
    CallNotify callNotify;



    @Transactional
    public String adhocCreateProcessTxn(TxnDetails txnDetails){

        if(txnDetails.getTxnType().equals("CREDIT")) {
            return adSbCbCreditTxnProcessing(txnDetails);
        } else if (txnDetails.getTxnType().equals("DEBIT")) {
            return adSbCbDebitTxnProcessing(txnDetails);
        }
        return adLoanTxnProcessing(txnDetails, false);
    }

    @Transactional
    public String adSbCbCreditTxnProcessing(TxnDetails txnDetails){
        SbCbdetail mainAcc = sbCbAccRepo.findAllByAccountID(txnDetails.getAccountID());
        if (!mainAcc.getAccStatus().equals("CLOSED")) {
            if (sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID()) != null) {
                SbCbdetail serviceAcc = sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID());
                if(!serviceAcc.getAccStatus().equals("CLOSED")) {
                    if ((serviceAcc.getAccBalance().subtract(txnDetails.getTxnAmount())).compareTo(BigDecimal.ZERO) > 0) {
                        txnDetails.setTxnStatus("SUCCESS");
                        txnDetails.setTxnMessage("Transaction Successful.");
                        txnDetails.setTxnDate(LocalDate.now());
                        txnRepo.save(txnDetails);
                        sbCbAccRepo.updateSbCbAccount((mainAcc.getAccBalance().add(txnDetails.getTxnAmount())), txnDetails.getTxnID(),
                                LocalDate.now(), LocalDateTime.now(), mainAcc.getAccountID());
                        String notify = iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                                clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                                "Credit transaction processed successfully on your account ID " + mainAcc.getAccountID() +
                                        " for transaction amount " + txnDetails.getTxnAmount() + ". Your current available balance is "
                                        + (mainAcc.getAccBalance().add(txnDetails.getTxnAmount())));
                        TxnDetails txnStructureForDebit = getTxnStructureForAdhocTxn(txnDetails);
                        System.out.println(txnStructureForDebit.getTxnAmount());
                        txnRepo.save(txnStructureForDebit);
                        sbCbAccRepo.updateSbCbAccount(serviceAcc.getAccBalance().subtract(txnStructureForDebit.getTxnAmount()), txnStructureForDebit.getTxnID(),
                                LocalDate.now(), LocalDateTime.now(), serviceAcc.getAccountID());
                        String notify2 = iAmNotifier(serviceAcc.getClientID(), serviceAcc.getAccountID(),
                                clientDetailRepo.findEmailByClientID(serviceAcc.getClientID()), clientDetailRepo.findContactByClientID(serviceAcc.getClientID()),
                                "Debit transaction processed successfully on your account ID " + serviceAcc.getAccountID() +
                                        " for transaction amount " + txnStructureForDebit.getTxnAmount() + ". Your current available balance is "
                                        + serviceAcc.getAccBalance().subtract(txnStructureForDebit.getTxnAmount()));
                        return notify + "\n" + notify2;
                    }
                    txnDetails.setTxnStatus("FAILED");
                    txnDetails.setTxnMessage("Transaction Unsuccessful.");
                    txnDetails.setTxnDate(LocalDate.now());
                    txnRepo.save(txnDetails);
                    return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                            clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                            "Credit transaction failed to process on your account ID " + mainAcc.getAccountID() +
                                    " for transaction amount " + txnDetails.getTxnAmount() +
                                    " due to insufficient balance in given source account ID " + serviceAcc.getAccountID() +
                                    " Your current available balance is "
                                    + mainAcc.getAccBalance());
                }
                txnDetails.setTxnStatus("FAILED");
                txnDetails.setTxnMessage("Transaction Unsuccessful.");
                txnDetails.setTxnDate(LocalDate.now());
                txnRepo.save(txnDetails);
                return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                        clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                        "Credit transaction failed to process on your account ID " + mainAcc.getAccountID() +
                                " for transaction amount " + txnDetails.getTxnAmount() +
                                " as the in given source account ID " + serviceAcc.getAccountID() +" is closed "+
                                " Your current available balance is "
                                + mainAcc.getAccBalance());
            }
            txnDetails.setTxnStatus("SUCCESS");
            txnDetails.setTxnMessage("Transaction Successful.");
            txnDetails.setTxnDate(LocalDate.now());
            txnRepo.save(txnDetails);
            sbCbAccRepo.updateSbCbAccount(mainAcc.getAccBalance().add(txnDetails.getTxnAmount()), txnDetails.getTxnID(),
                    LocalDate.now(), LocalDateTime.now(), mainAcc.getAccountID());
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Credit transaction processed successfully on your account ID "+mainAcc.getAccountID()+
                            "for transaction amount "+txnDetails.getTxnAmount()+". Your current available balance is "
                            +(mainAcc.getAccBalance().add(txnDetails.getTxnAmount())));
        }
        return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                "Credit transaction failed to process on your account ID "+mainAcc.getAccountID()+
                        " for transaction amount "+txnDetails.getTxnAmount()+ " as the account is closed.");
    }

    @Transactional
    public String adSbCbDebitTxnProcessing(TxnDetails txnDetails){
        SbCbdetail mainAcc = sbCbAccRepo.findAllByAccountID(txnDetails.getAccountID());
        if (!mainAcc.getAccStatus().equals("CLOSED")) {
        if(sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID()) != null) {
            SbCbdetail serviceAcc = sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID());
            if(!serviceAcc.getAccStatus().equals("CLOSED")) {
            if ((mainAcc.getAccBalance().subtract(txnDetails.getTxnAmount())).compareTo(BigDecimal.ZERO) > 0) {
                txnDetails.setTxnStatus("SUCCESS");
                txnDetails.setTxnMessage("Transaction Successful.");
                txnDetails.setTxnDate(LocalDate.now());
                txnRepo.save(txnDetails);
                sbCbAccRepo.updateSbCbAccount(mainAcc.getAccBalance().subtract(txnDetails.getTxnAmount()), txnDetails.getTxnID(),
                        LocalDate.now(), LocalDateTime.now(), mainAcc.getAccountID());
                String notify = iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                        clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                        "Debit transaction processed successfully on your account ID " + mainAcc.getAccountID() +
                                " for transaction amount " + txnDetails.getTxnAmount() + ". Your current available balance is "
                                + (mainAcc.getAccBalance().subtract(txnDetails.getTxnAmount())));
                TxnDetails txnStructureForDebit = getTxnStructureForAdhocTxn(txnDetails);
                txnRepo.save(txnStructureForDebit);
                sbCbAccRepo.updateSbCbAccount(serviceAcc.getAccBalance().add(txnStructureForDebit.getTxnAmount()), txnStructureForDebit.getTxnID(),
                        LocalDate.now(), LocalDateTime.now(), serviceAcc.getAccountID());
                String notify2 = iAmNotifier(serviceAcc.getClientID(), serviceAcc.getAccountID(),
                        clientDetailRepo.findEmailByClientID(serviceAcc.getClientID()), clientDetailRepo.findContactByClientID(serviceAcc.getClientID()),
                        "Credit transaction processed successfully on your account ID " + serviceAcc.getAccountID() +
                                " for transaction amount " + txnStructureForDebit.getTxnAmount() + ". Your current available balance is "
                                + serviceAcc.getAccBalance().add(txnStructureForDebit.getTxnAmount()));
                return notify + "\n" + notify2;
            }
            txnDetails.setTxnStatus("FAILED");
            txnDetails.setTxnMessage("Transaction Unsuccessful.");
            txnDetails.setTxnDate(LocalDate.now());
            txnRepo.save(txnDetails);
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Debit transaction failed to process on your account ID " + mainAcc.getAccountID() +
                            " for transaction amount " + txnDetails.getTxnAmount() +
                            " due to insufficient balance in on your account ID " + mainAcc.getAccountID() +
                            " Your current available balance is "
                            + mainAcc.getAccBalance());

            }
            txnDetails.setTxnStatus("FAILED");
            txnDetails.setTxnMessage("Transaction Unsuccessful.");
            txnDetails.setTxnDate(LocalDate.now());
            txnRepo.save(txnDetails);
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Debit transaction failed to process on your account ID " + mainAcc.getAccountID() +
                            " for transaction amount " + txnDetails.getTxnAmount() +
                            " as the in given destination account ID " + serviceAcc.getAccountID() +" is closed "+
                            " Your current available balance is "
                            + mainAcc.getAccBalance());
        }
        txnDetails.setTxnStatus("SUCCESS");
        txnDetails.setTxnMessage("Transaction Successful.");
        txnDetails.setTxnDate(LocalDate.now());
        txnRepo.save(txnDetails);
        sbCbAccRepo.updateSbCbAccount(mainAcc.getAccBalance().subtract(txnDetails.getTxnAmount()), txnDetails.getTxnID(),
                LocalDate.now(), LocalDateTime.now(),mainAcc.getAccountID());
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Debit transaction processed successfully on your account ID "+mainAcc.getAccountID()+
                            "for transaction amount "+txnDetails.getTxnAmount()+". Your current available balance is "
                            +(mainAcc.getAccBalance().subtract(txnDetails.getTxnAmount())));
    }
        return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                "Debit transaction failed to process on your account ID "+mainAcc.getAccountID()+
                        " for transaction amount "+txnDetails.getTxnAmount()+ " as the account is closed.");
    }

    @Transactional
    public String adLoanTxnProcessing(TxnDetails txnDetails, boolean boReturn){
        LoanAccDetail mainAcc = loanAccRepo.findAllByAccountID(txnDetails.getAccountID());
        if (!mainAcc.getAccStatus().equals("CLOSED")) {
            if (sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID()) != null) {
                SbCbdetail serviceAcc = sbCbAccRepo.findAllByAccountID(txnDetails.getPayingAccID());
                if(!serviceAcc.getAccStatus().equals("CLOSED")) {
                if ((serviceAcc.getAccBalance().subtract(txnDetails.getTxnAmount())).compareTo(BigDecimal.ZERO) > 0) {
                    txnDetails.setTxnStatus("SUCCESS");
                    txnDetails.setTxnMessage("Transaction Successful.");
                    txnDetails.setTxnDate(LocalDate.now());
                    txnRepo.save(txnDetails);
                    if (!boReturn) {
                        mainAcc.setLoanAmount(mainAcc.getLoanAmount().subtract(txnDetails.getTxnAmount()));
                        mainAcc.setAmountToBePaid(
                                mainAcc.getLoanAmount().multiply(
                                        BigDecimal.ONE.add(
                                                mainAcc.getIntrestRate().divide(BigDecimal.valueOf(100))
                                                        .multiply(BigDecimal.valueOf(mainAcc.getTenure())))));
                        mainAcc.setEmiAmount(
                                mainAcc.getAmountToBePaid().divide(
                                        BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(mainAcc.getTenure())),
                                        2,
                                        RoundingMode.HALF_UP));
                        mainAcc.setAmountPaidTillDate(mainAcc.getAmountPaidTillDate().add(txnDetails.getTxnAmount()));
                    } else {
                        mainAcc.setAmountToBePaid(mainAcc.getAmountToBePaid().subtract(txnDetails.getTxnAmount()));
                        mainAcc.setAmountPaidTillDate(mainAcc.getAmountPaidTillDate().add(txnDetails.getTxnAmount()));
                        mainAcc.setEmiCyclesCompleted(mainAcc.getEmiCyclesCompleted() + 1);
                        if (mainAcc.getAmountToBePaid().compareTo(mainAcc.getLoanAmount()) <= 0) {
                            mainAcc.setLoanAmount(mainAcc.getAmountToBePaid());
                        }

                    }
                    mainAcc.setLastDoneTxnID(txnDetails.getTxnID());
                    if (mainAcc.getAmountToBePaid().compareTo(BigDecimal.ZERO) == 0) {
                        mainAcc.setAccStatus("CLOSED");
                    }
                    loanAccRepo.updateLoanAccount(mainAcc.getLoanAmount(), mainAcc.getAmountToBePaid(),
                            mainAcc.getEmiAmount(), mainAcc.getAmountPaidTillDate(),
                            mainAcc.getLastDoneTxnID(), mainAcc.getAccStatus(), mainAcc.getEmiCyclesCompleted(), LocalDateTime.now(), mainAcc.getAccountID());
                    String schedule = updateScheduleEvent(mainAcc);
                    String notify = iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                            clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                            "Loan payment transaction processed successfully on your loan account ID " + mainAcc.getAccountID() +
                                    " for transaction amount " + txnDetails.getTxnAmount());
                    TxnDetails txnStructureForDebit = getTxnStructureForAdhocTxn(txnDetails);
                    txnRepo.save(txnStructureForDebit);
                    sbCbAccRepo.updateSbCbAccount(serviceAcc.getAccBalance().subtract(txnStructureForDebit.getTxnAmount()), txnStructureForDebit.getTxnID(),
                            LocalDate.now(), LocalDateTime.now(), serviceAcc.getAccountID());
                    String notify2 = iAmNotifier(serviceAcc.getClientID(), serviceAcc.getAccountID(),
                            clientDetailRepo.findEmailByClientID(serviceAcc.getClientID()), clientDetailRepo.findContactByClientID(serviceAcc.getClientID()),
                            "Debit transaction processed successfully on your account ID " + serviceAcc.getAccountID() +
                                    " for transaction amount " + txnStructureForDebit.getTxnAmount() + " against loan payment for your loan account ID " +
                                    mainAcc.getAccountID() + ". Your current available balance is "
                                    + serviceAcc.getAccBalance().subtract(txnStructureForDebit.getTxnAmount()));
                    return notify + "\n" + notify2 + "\n" + schedule;

                }
                txnDetails.setTxnStatus("FAILED");
                txnDetails.setTxnMessage("Transaction Unsuccessful.");
                txnDetails.setTxnDate(LocalDate.now());
                txnRepo.save(txnDetails);
                return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                        clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                        "Loan payment transaction failed to process on your loan account ID " + mainAcc.getAccountID() +
                                " for transaction amount " + txnDetails.getTxnAmount() +
                                " due to insufficient balance in given source account ID " + serviceAcc.getAccountID());
            }

            txnDetails.setTxnStatus("FAILED");
            txnDetails.setTxnMessage("Transaction Unsuccessful.");
            txnDetails.setTxnDate(LocalDate.now());
            txnRepo.save(txnDetails);
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()), clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Loan payment transaction failed to process on your loan account ID " + mainAcc.getAccountID() +
                            " for transaction amount " + txnDetails.getTxnAmount() +
                            " as the in given source account ID " + serviceAcc.getAccountID() +" is closed ");

        }
            txnDetails.setTxnStatus("SUCCESS");
            txnDetails.setTxnMessage("Transaction Successful.");
            txnDetails.setTxnDate(LocalDate.now());
            txnRepo.save(txnDetails);
            if(!boReturn) {
                mainAcc.setLoanAmount(mainAcc.getLoanAmount().subtract(txnDetails.getTxnAmount()));
                mainAcc.setAmountToBePaid(
                        mainAcc.getLoanAmount().multiply(
                                BigDecimal.ONE.add(
                                        mainAcc.getIntrestRate().divide(BigDecimal.valueOf(100))
                                                .multiply(BigDecimal.valueOf(mainAcc.getTenure())))));
                mainAcc.setEmiAmount(
                        mainAcc.getAmountToBePaid().divide(
                                BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(mainAcc.getTenure())),
                                2,
                                RoundingMode.HALF_UP));
                mainAcc.setAmountPaidTillDate(mainAcc.getAmountPaidTillDate().add(txnDetails.getTxnAmount()));
            }else {
                mainAcc.setAmountToBePaid(mainAcc.getAmountToBePaid().subtract(txnDetails.getTxnAmount()));
                mainAcc.setAmountPaidTillDate(mainAcc.getAmountPaidTillDate().add(txnDetails.getTxnAmount()));
                mainAcc.setEmiCyclesCompleted(mainAcc.getEmiCyclesCompleted() + 1);
                if(mainAcc.getAmountToBePaid().compareTo(mainAcc.getLoanAmount()) <= 0){
                    mainAcc.setLoanAmount(mainAcc.getAmountToBePaid());
                }
            }
            mainAcc.setLastDoneTxnID(txnDetails.getTxnID());
            if(mainAcc.getAmountToBePaid().compareTo(BigDecimal.ZERO) == 0){
                mainAcc.setAccStatus("CLOSED");
            }
            loanAccRepo.updateLoanAccount(mainAcc.getLoanAmount(), mainAcc.getAmountToBePaid(),
                    mainAcc.getEmiAmount(), mainAcc.getAmountPaidTillDate(),
                    mainAcc.getLastDoneTxnID(), mainAcc.getAccStatus(), mainAcc.getEmiCyclesCompleted(), LocalDateTime.now(), mainAcc.getAccountID());
            String schedule = updateScheduleEvent(mainAcc);
            return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                    clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                    "Loan payment transaction processed successfully on your loan account ID " + mainAcc.getAccountID() +
                            " for transaction amount " + txnDetails.getTxnAmount());

        }
        return iAmNotifier(mainAcc.getClientID(), mainAcc.getAccountID(),
                clientDetailRepo.findEmailByClientID(mainAcc.getClientID()),clientDetailRepo.findContactByClientID(mainAcc.getClientID()),
                "Loan payment transaction failed to process on your loan account ID "+mainAcc.getAccountID()+
                        " for transaction amount "+txnDetails.getTxnAmount()+ " as the loan account is closed.");
    }

    TxnDetails getTxnStructureForAdhocTxn(TxnDetails txnDetails){
        TxnDetails txnSource = new TxnDetails();
        txnSource.setAccountID(txnDetails.getPayingAccID());
        txnSource.setPayingAccID(txnDetails.getAccountID());
        txnSource.setTxnAmount(txnDetails.getTxnAmount());
        txnSource.setTxnDate(LocalDate.now());
        txnSource.setTxnStatus(txnDetails.getTxnStatus());
        if(txnDetails.getTxnType().equals("CREDIT")){
            txnSource.setTxnType("DEBIT");
            txnSource.setTxnReason("Amount payed to account ID "+txnDetails.getAccountID());
        }else if (txnDetails.getTxnType().equals("DEBIT")){
            txnSource.setTxnType("CREDIT");
            txnSource.setTxnReason("Amount received from account ID "+txnDetails.getTxnID());
        }else {
            txnSource.setTxnType("DEBIT");
            txnSource.setTxnReason("Amount payed to Loan account ID "+txnDetails.getAccountID());
        }
        txnSource.setTxnMessage(txnDetails.getTxnMessage());
        return txnSource;
    }

    @Transactional
    String updateScheduleEvent(LoanAccDetail loanAccDetail){
        ScheduleEvents scheduleEvents = scheduleEventsRepo.findAllByAccountID(loanAccDetail.getAccountID());
        scheduleEvents.setLastTxnDate(LocalDate.now());
        scheduleEvents.setTxnAmount(loanAccDetail.getEmiAmount());
        if(loanAccDetail.getAccStatus().equals("CLOSED")){
            scheduleEvents.setEventStatus("CLOSED");
        }
        scheduleEventsRepo.updateScheduleEvent(scheduleEvents.getLastTxnDate(), scheduleEvents.getTxnAmount(),
                scheduleEvents.getEventStatus(), LocalDateTime.now(), loanAccDetail.getAccountID());

        return "Event For Account ID "+loanAccDetail.getAccountID()+ "is updated.";

    }

    @Transactional
    public String autoCreateProcessTxn(){
        for(ScheduleEvents scheduleEvents : scheduleEventsRepo.getEventsByDate(LocalDate.now())){

                TxnDetails txnDetails = new TxnDetails();
                txnDetails.setAccountID(scheduleEvents.getAccountID());
                txnDetails.setPayingAccID(scheduleEvents.getPayerAccountID());
                txnDetails.setTxnType(scheduleEvents.getTxnType());
                txnDetails.setTxnAmount(scheduleEvents.getTxnAmount());
                txnDetails.setTxnReason("Transaction processing against your "+scheduleEvents.getTxnType()+" schedule event for account ID "+scheduleEvents.getAccountID());
                txnDetails.setTxnDate(LocalDate.now());
                if(scheduleEvents.getTxnType().equals("LOAN")){
                    adLoanTxnProcessing(txnDetails,true);
                } else if (scheduleEvents.getTxnType().equals("CREDIT")) {
                    adSbCbCreditTxnProcessing(txnDetails);
                } else if (scheduleEvents.getTxnType().equals("DEBIT")) {
                    adSbCbDebitTxnProcessing(txnDetails);
                }
                scheduleEvents.setNextTxnDate(scheduleEvents.getNextTxnDate().plusMonths(1));
                scheduleEventsRepo.updateScheduleEventByAutoTxn(scheduleEvents.getNextTxnDate(), LocalDateTime.now(),scheduleEvents.getEventID());
        }

        return "Automated txn processed successfully.";
    }

    public List<TxnDetails> getAllTxnByAccId(String accountType, Long accountID){
        if(accountType.equals("LOAN")){
            return Optional.ofNullable(txnRepo.getLoanTxn(accountID))
                    .orElse(Collections.emptyList());
        }
        return Optional.ofNullable(txnRepo.getSbCbTxn(accountID))
                .orElse(Collections.emptyList());
    }

    public String iAmNotifier(Long clientId, Long accountId, String email, Long contact, String message){
        NotifyDetail notifyDetail = new NotifyDetail();
        notifyDetail.setAccountId(accountId);
        notifyDetail.setClientId(clientId);
        notifyDetail.setContact(contact);
        notifyDetail.setEmail(email);
        notifyDetail.setMessage(message);
        return callNotify.notifyClientByDetails(notifyDetail);
    }

}
