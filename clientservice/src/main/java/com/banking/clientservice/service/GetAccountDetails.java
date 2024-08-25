package com.banking.clientservice.service;


import com.banking.clientservice.Entities.LoanAccDetail;
import com.banking.clientservice.Entities.SbCbDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SERVICE-ACCOUNT")
public interface GetAccountDetails {

    @GetMapping("/AccountService/SbCbAccount/ClientID/{accountId}")
    List<SbCbDetail> getAllSbCbAccByClientID(@PathVariable Long accountId);

    @GetMapping("/AccountService/LoanAccount/ClientID/{clientId}")
    public List<LoanAccDetail> getAllLoanAccByClientID(@PathVariable Long clientId);


}
