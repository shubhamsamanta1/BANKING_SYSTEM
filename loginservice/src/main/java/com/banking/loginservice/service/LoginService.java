package com.banking.loginservice.service;

import com.banking.loginservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoginService {

    @Autowired
    ClientRepository clientRepository;

    public String userLogin(Long ID , String password){
        if(!(clientRepository.findByclientID(ID) .isEmpty()) && !(clientRepository.findByPassword(password).isEmpty())){
            return "LOGIN SUCCESS: WELCOME";
        }
        else if((clientRepository.findByclientID(ID).isEmpty()) && !(clientRepository.findByPassword(password).isEmpty())){
            return "LOGIN FAILED: INCORRECT CLIENT ID";
        }
        else if(!(clientRepository.findByclientID(ID).isEmpty()) && (clientRepository.findByPassword(password).isEmpty())){
            return "LOGIN FAILED: INCORRECT PASSWORD";
        }
        return "LOGIN FAILED: CLIENT DOESN'T EXIST";
    }

    @Transactional
    public String passwordReset(long ID, String password){
        if(!(clientRepository.findByclientID(ID).isEmpty())){
            LocalDateTime currentTimestamp = LocalDateTime.now();
            clientRepository.updatePassword(ID, password, currentTimestamp);
            return "PASSWORD UPDATE SUCCESS : PASSWORD IS UPDATED";
        }
        return "PASSWORD UPDATE FAILED: INCORRECT CLIENT ID OR CLIENT DOESN'T EXIST";
    }
}
