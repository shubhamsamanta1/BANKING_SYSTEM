package com.banking.accoutservice.service;

import com.banking.accoutservice.entities.LoanAccDetail;
import com.banking.accoutservice.entities.NotifyDetail;
import com.banking.accoutservice.entities.SbCbdetail;
import com.banking.accoutservice.entities.ScheduleEvents;
import com.banking.accoutservice.repository.ClientDetailRepo;
import com.banking.accoutservice.repository.LoanAccRepo;
import com.banking.accoutservice.repository.SbCbAccRepo;
import com.banking.accoutservice.repository.ScheduleEventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    SbCbAccRepo sbCbAccRepo ;

    @Autowired
    ScheduleEventsRepo scheduleEventsRepo;

    @Autowired
    LoanAccRepo loanAccRepo;

    @Autowired
    CallNotify callNotify;

    @Autowired
    ClientDetailRepo clientDetailRepo;

    public String createUpdateSbCbAccount(SbCbdetail  sbCbdetail){

        if(sbCbdetail.getAccountID() == null) {
            sbCbAccRepo.save(sbCbdetail);
            if (sbCbdetail.getAccType().equals("SAVINGS")) {

                return iAmNotifier(sbCbdetail.getClientID(), sbCbdetail.getAccountID(),
                        clientDetailRepo.findEmailByClientID(sbCbdetail.getClientID()),
                        clientDetailRepo.findContactByClientID(sbCbdetail.getClientID()),"Savings account for your clientID "+ sbCbdetail.getClientID() +" is Created Successfully");
            }
            return iAmNotifier(sbCbdetail.getClientID(), sbCbdetail.getAccountID(),
                    clientDetailRepo.findEmailByClientID(sbCbdetail.getClientID()),
                    clientDetailRepo.findContactByClientID(sbCbdetail.getClientID()),"Current account for your clientID "+ sbCbdetail.getClientID() +" is Created Successfully");
            }

        sbCbAccRepo.save(sbCbdetail);
        if (sbCbdetail.getAccType().equals("SAVINGS")) {
            return iAmNotifier(sbCbdetail.getClientID(), sbCbdetail.getAccountID(),
                    clientDetailRepo.findEmailByClientID(sbCbdetail.getClientID()),
                    clientDetailRepo.findContactByClientID(sbCbdetail.getClientID()),"Savings account for your clientID "+ sbCbdetail.getClientID() +" is Updated Successfully");
        }
        return iAmNotifier(sbCbdetail.getClientID(), sbCbdetail.getAccountID(),
                clientDetailRepo.findEmailByClientID(sbCbdetail.getClientID()),
                clientDetailRepo.findContactByClientID(sbCbdetail.getClientID()),"Current account for your clientID "+ sbCbdetail.getClientID() +" is Updated Successfully");

    }

    public List<SbCbdetail> getAllSbCbAccByClientID(Long clientId){
        return sbCbAccRepo.findAllByClientID(clientId);
    }

    public SbCbdetail getSbCbAccDetailsByAccID(Long accountID){
        return sbCbAccRepo.findAllByAccountID(accountID);
    }

    public String CreateUpdateScheduleEvents(ScheduleEvents scheduleEvents){
        if (scheduleEvents.getEventID() == null){
            scheduleEventsRepo.save(scheduleEvents);
            return iAmNotifier(scheduleEvents.getClientID(), scheduleEvents.getAccountID(),
                    clientDetailRepo.findEmailByClientID(scheduleEvents.getClientID()),
                    clientDetailRepo.findContactByClientID(scheduleEvents.getClientID()),"Schedule event For your Account ID "+scheduleEvents.getAccountID()+" is Created Successfully");
        }
        scheduleEventsRepo.save(scheduleEvents);
        return iAmNotifier(scheduleEvents.getClientID(), scheduleEvents.getAccountID(),
                clientDetailRepo.findEmailByClientID(scheduleEvents.getClientID()),
                clientDetailRepo.findContactByClientID(scheduleEvents.getClientID()),"Schedule event For your Account ID "+scheduleEvents.getAccountID()+" is Updated Successfully");

    }

    public List<ScheduleEvents> getAllScheduleEventsByAccId(Long accountID){
        return scheduleEventsRepo.findAllByAccountID(accountID);
    }

    public ScheduleEvents getAnEventAfterLoanAccountCreation(LoanAccDetail loanAccDetail){
        ScheduleEvents scheduleEvents = new ScheduleEvents();
        scheduleEvents.setAccountID(loanAccDetail.getAccountID());
        scheduleEvents.setPayerAccountID(loanAccDetail.getPaymentSourceBankID());
        scheduleEvents.setTxnType("LOAN");
        scheduleEvents.setClientID(loanAccDetail.getClientID());
        scheduleEvents.setTxnAmount(loanAccDetail.getEmiAmount());
        scheduleEvents.setEventStatus("ACTIVE");
        scheduleEvents.setEventReason("Emi Schedule event against your Loan account ID "+loanAccDetail.getAccountID());
        LocalDate localDate = LocalDate.now();
        scheduleEvents.setEventStartDate(localDate);
        LocalDate nextTxnDate = loanAccDetail.getEmiCycleDate();
        scheduleEvents.setNextTxnDate(nextTxnDate.plusMonths(1));
        scheduleEvents.setEventEndDate(localDate.plusYears(loanAccDetail.getTenure()));
        return scheduleEvents;
    }

    @Transactional
    public  String createUpdateLoanAccount(LoanAccDetail loanAccDetail){
        if(loanAccDetail.getAccountID() == null) {
            loanAccDetail.setAmountToBePaid(
                    loanAccDetail.getLoanAmount().multiply(
                            BigDecimal.ONE.add(
                                    loanAccDetail.getIntrestRate().divide(BigDecimal.valueOf(100))
                                            .multiply(BigDecimal.valueOf(loanAccDetail.getTenure())))));
            loanAccDetail.setEmiAmount(
                    loanAccDetail.getAmountToBePaid().divide(
                            BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(loanAccDetail.getTenure())),
                            2,
                            RoundingMode.HALF_UP));
            loanAccRepo.save(loanAccDetail);
            String response = iAmNotifier(loanAccDetail.getClientID(), loanAccDetail.getAccountID(),
                    clientDetailRepo.findEmailByClientID(loanAccDetail.getClientID()),
                    clientDetailRepo.findContactByClientID(loanAccDetail.getClientID()),"Loan account for your clientID "+ loanAccDetail.getClientID() +" is Created Successfully");
            CreateUpdateScheduleEvents(getAnEventAfterLoanAccountCreation(loanAccDetail));
            return response;
        }
        loanAccDetail.setAmountToBePaid(
                loanAccDetail.getLoanAmount().multiply(
                        BigDecimal.ONE.add(
                                loanAccDetail.getIntrestRate().divide(BigDecimal.valueOf(100))
                                        .multiply(BigDecimal.valueOf(loanAccDetail.getTenure())))));
        loanAccDetail.setEmiAmount(
                loanAccDetail.getAmountToBePaid().divide(
                        BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(loanAccDetail.getTenure())),
                        2,
                        RoundingMode.HALF_UP));
        scheduleEventsRepo.updateScheduleEvent(loanAccDetail.getEmiAmount(), LocalDateTime.now(),loanAccDetail.getAccountID());

        loanAccRepo.save(loanAccDetail);
        return iAmNotifier(loanAccDetail.getClientID(), loanAccDetail.getAccountID(),
                clientDetailRepo.findEmailByClientID(loanAccDetail.getClientID()),
                clientDetailRepo.findContactByClientID(loanAccDetail.getClientID()),"Loan account for your clientID "+ loanAccDetail.getClientID() +" is Updated Successfully");

    }

    public List<LoanAccDetail> getAllLoanAccByClientID(Long clientId){
        return loanAccRepo.findAllByClientID(clientId);
    }

    public LoanAccDetail getLoanAccDetailsByAccID(Long accountID){
        return loanAccRepo.findAllByAccountID(accountID);
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

