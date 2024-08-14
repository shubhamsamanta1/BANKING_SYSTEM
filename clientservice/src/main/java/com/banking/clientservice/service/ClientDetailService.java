package com.banking.clientservice.service;

import com.banking.clientservice.Entities.ClientDetail;
import com.banking.clientservice.Entities.NotifyDetail;
import com.banking.clientservice.repository.ClientDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientDetailService {

    @Autowired
    ClientDetailRepo clientDetailRepo;

    @Autowired
    CallNotify callNotify;

    @Autowired
    GetAccountDetails getAccountDetails;

    public String createUpdateClient(ClientDetail clientDetail){
        if(clientDetail.getClientID() == null){
            clientDetailRepo.save(clientDetail);
            return iAmNotifier(clientDetail.getClientID(), clientDetail.getEmail(), clientDetail.getContact(),"New client created with client ID "+clientDetail.getClientID());
        }
            clientDetailRepo.save(clientDetail);
            return iAmNotifier(clientDetail.getClientID(), clientDetail.getEmail(), clientDetail.getContact(),"Updated client details for client ID "+clientDetail.getClientID());

    }

    public ClientDetail getClientDetails (Long clientID){
            ClientDetail client = clientDetailRepo.findById(clientID).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
            client.setSbCbDetails(getAccountDetails.getAllSbCbAccByClientID(clientID));
            client.setLoanAccDetails(getAccountDetails.getAllLoanAccByClientID(clientID));
            return  client;
    }

    public String iAmNotifier(Long clientId, String email, Long contact, String message){
        NotifyDetail notifyDetail = new NotifyDetail();
        notifyDetail.setClientId(clientId);
        notifyDetail.setEmail(email);
        notifyDetail.setContact(contact);
        notifyDetail.setAccountId((long) 9999);
        notifyDetail.setMessage(message);
        return callNotify.notifyClientByDetails(notifyDetail);
    }


}
