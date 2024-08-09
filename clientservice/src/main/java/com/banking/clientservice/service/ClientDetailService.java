package com.banking.clientservice.service;

import com.banking.clientservice.Entities.ClientDetail;
import com.banking.clientservice.repository.ClientDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientDetailService {

    @Autowired
    ClientDetailRepo clientDetailRepo;


    public ClientDetail createUpdateClient(ClientDetail clientDetail){
        return clientDetailRepo.save(clientDetail);
    }

    public ClientDetail getClientDetails (Long clientID){
            return clientDetailRepo.findById(clientID).orElseThrow(() -> new RuntimeException("NOT_FOUND")) ;
    }


}
