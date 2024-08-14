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
            return iAmNotifier(clientDetail.getClientID(),clientRepository.findEmailByClientID(clientDetail.getClientID()),
                    clientRepository.findContactByClientID(clientDetail.getClientID()),
                    "Login Successful for client ID "+clientDetail.getClientID()+" ,Welcome.");
        }
        else if((clientRepository.findByclientID(clientDetail.getClientID()).isEmpty()) && !(clientRepository.findByPassword(clientDetail.getPassword()).isEmpty())){
            return "LOGIN FAILED: INCORRECT CLIENT ID";
        }
        else if(!(clientRepository.findByclientID(clientDetail.getClientID()).isEmpty()) && (clientRepository.findByPassword(clientDetail.getPassword()).isEmpty())){
            return iAmNotifier(clientDetail.getClientID(),clientRepository.findEmailByClientID(clientDetail.getClientID()),
                    clientRepository.findContactByClientID(clientDetail.getClientID()),
                    "Login unsuccessful incorrect password for client ID "+clientDetail.getClientID()+" ,Welcome.");
        }
        return "LOGIN FAILED: CLIENT DOESN'T EXIST";
    }

    @Transactional
    public String passwordReset(ClientDetail clientDetail){
        if(!(clientRepository.findByclientID(clientDetail.getClientID()).isEmpty())){
            LocalDateTime currentTimestamp = LocalDateTime.now();
            clientRepository.updatePassword(clientDetail.getClientID(), clientDetail.getPassword(), currentTimestamp);
            return iAmNotifier(clientDetail.getClientID(),clientRepository.findEmailByClientID(clientDetail.getClientID()),
                    clientRepository.findContactByClientID(clientDetail.getClientID()),
                    "Password updated successfully for client ID "+clientDetail.getClientID());
        }
        return "PASSWORD UPDATE FAILED: INCORRECT CLIENT ID OR CLIENT DOESN'T EXIST";
    }

    public String getClientIdByGovId(String govid){
        if(!(clientRepository.findByGovId(govid) == null)){
            ClientDetail clientDetail = clientRepository.findByGovId(govid);
            return iAmNotifier(clientDetail.getClientID(),clientRepository.findEmailByClientID(clientDetail.getClientID()),
                    clientRepository.findContactByClientID(clientDetail.getClientID()),
                    "Requested client ID: " + clientDetail.getClientID() + " is linked to the provided GOV ID: " + govid);
        }
        return "INCORRECT GOV ID OR CLIENT DOESN'T EXIST";
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
