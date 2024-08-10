package com.banking.loginservice.service;

import com.banking.loginservice.entites.ClientDetail;
import com.banking.loginservice.entites.NotifyDetail;
import com.banking.loginservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoginService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CallNotify callNotify;

    public String userLogin(ClientDetail clientDetail){
        if(!(clientRepository.findByclientID(clientDetail.getClientID()) .isEmpty()) && !(clientRepository.findByPassword(clientDetail.getPassword()).isEmpty())){
            NotifyDetail notifyDetail = new NotifyDetail();
            notifyDetail.setClientId(clientDetail.getClientID());
            notifyDetail.setEmail(clientRepository.findByEmail(clientDetail.getClientID()));
            notifyDetail.setContact(clientRepository.findByContact(clientDetail.getClientID()));
            notifyDetail.setAccountId((long) 9999); //dummy account
            notifyDetail.setMessage("LOGIN SUCCESS: WELCOME");
            return callNotify.notifyClientByDetails(notifyDetail);
        }
        else if((clientRepository.findByclientID(clientDetail.getClientID()).isEmpty()) && !(clientRepository.findByPassword(clientDetail.getPassword()).isEmpty())){
            return "LOGIN FAILED: INCORRECT CLIENT ID";
        }
        else if(!(clientRepository.findByclientID(clientDetail.getClientID()).isEmpty()) && (clientRepository.findByPassword(clientDetail.getPassword()).isEmpty())){
            NotifyDetail notifyDetail = new NotifyDetail();
            notifyDetail.setClientId(clientDetail.getClientID());
            notifyDetail.setEmail(clientRepository.findByEmail(clientDetail.getClientID()));
            notifyDetail.setContact(clientRepository.findByContact(clientDetail.getClientID()));
            notifyDetail.setAccountId((long) 9999); //dummy account
            notifyDetail.setMessage("LOGIN FAILED: INCORRECT PASSWORD");
            return callNotify.notifyClientByDetails(notifyDetail);
        }
        return "LOGIN FAILED: CLIENT DOESN'T EXIST";
    }

    @Transactional
    public String passwordReset(ClientDetail clientDetail){
        if(!(clientRepository.findByclientID(clientDetail.getClientID()).isEmpty())){
            LocalDateTime currentTimestamp = LocalDateTime.now();
            clientRepository.updatePassword(clientDetail.getClientID(), clientDetail.getPassword(), currentTimestamp);
            NotifyDetail notifyDetail = new NotifyDetail();
            notifyDetail.setClientId(clientDetail.getClientID());
            notifyDetail.setEmail(clientRepository.findByEmail(clientDetail.getClientID()));
            notifyDetail.setContact(clientRepository.findByContact(clientDetail.getClientID()));
            notifyDetail.setAccountId((long) 9999); //dummy account
            notifyDetail.setMessage("PASSWORD UPDATE SUCCESS : PASSWORD IS UPDATED");
            return callNotify.notifyClientByDetails(notifyDetail);
        }
        return "PASSWORD UPDATE FAILED: INCORRECT CLIENT ID OR CLIENT DOESN'T EXIST";
    }
}
