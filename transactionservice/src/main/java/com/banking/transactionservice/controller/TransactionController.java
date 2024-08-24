package com.banking.transactionservice.controller;

import com.banking.transactionservice.entities.TxnDetails;
import com.banking.transactionservice.sevice.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactionService")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/adhocTxn")
    public String adhocCreateProcessTxn(@RequestBody TxnDetails txnDetails){
        return transactionService.adhocCreateProcessTxn(txnDetails);
    }

    @GetMapping("/autoTxn")
    public String autoCreateProcessTxn(){
        return transactionService.autoCreateProcessTxn();
    }

    @GetMapping("/{accountType}/{accountID}")
    public List<TxnDetails> getAllTxnByAccId(@PathVariable String accountType, @PathVariable Long accountID){
        return transactionService.getAllTxnByAccId(accountType,accountID);
    }
}
