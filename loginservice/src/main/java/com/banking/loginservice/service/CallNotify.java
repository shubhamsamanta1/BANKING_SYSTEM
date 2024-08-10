package com.banking.loginservice.service;

import com.banking.loginservice.entites.NotifyDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8085" , name = "NOTIFY-LOGIN")
public interface CallNotify {

    @PostMapping("/notify")
    public String notifyClientByDetails(@RequestBody NotifyDetail notifyDetail);
}
