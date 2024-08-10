package com.banking.noticationservice.controller;

import com.banking.noticationservice.entities.NotifyDetail;
import com.banking.noticationservice.service.NotifyService;
import jakarta.persistence.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notify")
public class notifycontroller {

    @Autowired
    NotifyService notifyService;

    @PostMapping
    public String notifyClientByDetails(@RequestBody NotifyDetail notifyDetail){
        return notifyService.notifyClientByDetails(notifyDetail);
    }

    @GetMapping("account/{accountID}")
    public List<NotifyDetail> getAllNotificationsByAccountId(@PathVariable Long accountID){
        return notifyService.getAllNotificationsByAccountId(accountID);
    }

    @GetMapping("client/{clientId}")
    public List<NotifyDetail> getAllNotificationsByClientId(@PathVariable Long clientId){
        return notifyService.getAllNotificationsByClientId(clientId);
    }
}
