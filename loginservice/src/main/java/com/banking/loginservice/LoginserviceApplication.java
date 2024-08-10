package com.banking.loginservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoginserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginserviceApplication.class, args);
	}

}
