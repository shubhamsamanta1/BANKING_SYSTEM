package com.banking.accoutservice.service;

import com.banking.accoutservice.entities.NotifyDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SERVICE-NOTIFY")
public interface CallNotify {

    @PostMapping("/notify")
    public String notifyClientByDetails(@RequestBody NotifyDetail notifyDetail);
}