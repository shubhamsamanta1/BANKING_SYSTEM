package com.banking.noticationservice.service;

import com.banking.noticationservice.entities.NotifyDetail;
import com.banking.noticationservice.repository.NotifyDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyService {

    @Autowired
    NotifyDetailRepo notifyDetailRepo;

    @Autowired
    JavaMailSender javaMailSender;

    public String notifyClientByDetails(NotifyDetail notifyDetail){
        notifyDetailRepo.save(notifyDetail);
        String reason = notifyDetail.getMessage();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("developmenttester19@gmail.com");
        message.setTo(notifyDetail.getEmail());
        message.setSubject("UPDATE FROM BANK");
        message.setText("Dear Customer, \n\n \t"+reason+"\n\nThanks & Regards, \n BANK");
        javaMailSender.send(message);
        return "NOTIFICATION SENT AND RECORD SUCCESSFULLY, REASON: "+reason;
    }

    public List<NotifyDetail> getAllNotificationsByAccountId(Long accountId){
        return notifyDetailRepo.findAllByaccountId(accountId);
    }

    public List<NotifyDetail> getAllNotificationsByClientId(Long clientID){
        return notifyDetailRepo.findAllByclientId(clientID);
    }
}