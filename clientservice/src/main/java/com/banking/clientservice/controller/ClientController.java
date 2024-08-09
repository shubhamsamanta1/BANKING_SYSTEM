package com.banking.clientservice.controller;

import com.banking.clientservice.Entities.ClientDetail;
import com.banking.clientservice.service.ClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientService")
public class ClientController {

    @Autowired
    ClientDetailService clientDetailService;

    @PostMapping
    public ClientDetail createUpdateClient(@RequestBody ClientDetail clientDetail){
        return clientDetailService.createUpdateClient(clientDetail);
    }

    @GetMapping("/{id}")
    public ClientDetail getClientDetails(@PathVariable Long id){
        return clientDetailService.getClientDetails(id);
    }



}