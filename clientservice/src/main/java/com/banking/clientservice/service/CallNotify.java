package com.banking.clientservice.service;


import com.banking.clientservice.Entities.NotifyDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8085" , name = "NOTIFY-CLIENT")
public interface CallNotify {

    @PostMapping("/notify")
    public String notifyClientByDetails(@RequestBody NotifyDetail notifyDetail);
}
