package com.banking.accoutservice.controller;

import com.banking.accoutservice.entities.LoanAccDetail;
import com.banking.accoutservice.entities.SbCbdetail;
import com.banking.accoutservice.entities.ScheduleEvents;
import com.banking.accoutservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/AccountService")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/SbCbAccount")
    public String createUpdateSbCbAccount(@RequestBody SbCbdetail sbCbdetail){
        return accountService.createUpdateSbCbAccount(sbCbdetail);
    }

    @GetMapping("/SbCbAccount/ClientID/{accountId}")
    public List<SbCbdetail> getAllSbCbAccByClientID(@PathVariable Long accountId){
        return accountService.getAllSbCbAccByClientID(accountId);
    }

    @GetMapping("/SbCbAccount/AccountID/{clientId}")
    public SbCbdetail getAllSbCbAccByAccountID(@PathVariable Long clientId){
        return accountService.getSbCbAccDetailsByAccID(clientId);
    }

    @PostMapping("/ScheduleEvents")
    public String CreateUpdateScheduleEvents(@RequestBody ScheduleEvents scheduleEvents){
        return accountService.CreateUpdateScheduleEvents(scheduleEvents);
    }

    @GetMapping("/ScheduleEvents/{eventId}")
    public List<ScheduleEvents> getAllScheduleEventsByAccId(@PathVariable Long eventId){
        return accountService.getAllScheduleEventsByAccId(eventId);
    }

    @PostMapping("/LoanAccount")
    public  String createUpdateLoanAccount(@RequestBody LoanAccDetail loanAccDetail){
        return  accountService.createUpdateLoanAccount(loanAccDetail);
    }

    @GetMapping("/LoanAccount/ClientID/{clientId}")
    public List<LoanAccDetail> getAllLoanAccByClientID(@PathVariable Long clientId){
        return accountService.getAllLoanAccByClientID(clientId);
    }

    @GetMapping("/LoanAccount/AccountID/{accountID}")
    public LoanAccDetail getLoanAccDetailsByAccID(@PathVariable Long accountID){
        return accountService.getLoanAccDetailsByAccID(accountID);
    }


}
