package com.banking.noticationservice.service;

import com.banking.noticationservice.entities.NotifyDetail;
import com.banking.noticationservice.repository.NotifyDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyService {

    @Autowired
    NotifyDetailRepo notifyDetailRepo;

    public String notifyClientByDetails(NotifyDetail notifyDetail){
        notifyDetailRepo.save(notifyDetail);
        Long clientID = notifyDetail.getClientId();
        Long accountID = notifyDetail.getAccountId();
        String reason = notifyDetail.getMessage();
        String rstring;
        if(notifyDetail.getAccountId() == 9999){
            rstring  = "NOTIFIED CLIENT ID: "+clientID+" SUCCESSFULLY, REASON: "+reason;
        }
        else {
            rstring = "NOTIFIED CLIENT ID: "+clientID+" FOR ACCOUNT ID: "+accountID+" SUCCESSFULLY, REASON: "+reason;
        }
        return rstring;
    }

    public List<NotifyDetail> getAllNotificationsByAccountId(Long accountId){
        return notifyDetailRepo.findAllByaccountId(accountId);
    }

    public List<NotifyDetail> getAllNotificationsByClientId(Long clientID){
        return notifyDetailRepo.findAllByclientId(clientID);
    }
}
