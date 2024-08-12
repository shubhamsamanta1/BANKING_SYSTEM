package com.banking.loginservice.controller;

import com.banking.loginservice.entites.ClientDetail;
import com.banking.loginservice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginservice")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/userlogin")
    public String userLogin(@RequestBody ClientDetail clientDetail){
        return loginService.userLogin(clientDetail);
    }

    @PostMapping("/updatepassword")
    public String passwordReset(@RequestBody ClientDetail clientDetail){
        return loginService.passwordReset(clientDetail);
    }

    @GetMapping("/getclientid/{govId}")
    public String getClientIdByGovId(@PathVariable String govId){
        return loginService.getClientIdByGovId(govId);
    }


}
