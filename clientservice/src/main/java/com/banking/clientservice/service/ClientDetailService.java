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


    public String createUpdateClient(ClientDetail clientDetail){

        NotifyDetail notifyDetail = new NotifyDetail();
        notifyDetail.setClientId(clientDetail.getClientID());
        notifyDetail.setEmail(clientDetail.getEmail());
        notifyDetail.setContact(clientDetail.getContact());
        notifyDetail.setAccountId((long) 9999);//dummy account
        if(clientDetail.getClientID() == null){
            clientDetailRepo.save(clientDetail);
            notifyDetail.setClientId(clientDetail.getClientID());
            notifyDetail.setMessage("NEW CLIENT CREATED.");
        }
        else {
            if(clientDetailRepo.findById(clientDetail.getClientID()).isEmpty()){
                return "CLIENT UPDATE FAILED: INCORRECT CLIENT ID OR CLIENT DOESN'T EXIST";
            }
            else {
                clientDetailRepo.save(clientDetail);
                notifyDetail.setMessage("CLIENT DETAILS UPDATED");
            }
        }
        return callNotify.notifyClientByDetails(notifyDetail);
    }

    public ClientDetail getClientDetails (Long clientID){
            return clientDetailRepo.findById(clientID).orElseThrow(() -> new RuntimeException("NOT_FOUND")) ;
    }


}
