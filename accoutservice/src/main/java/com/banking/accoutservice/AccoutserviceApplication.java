package com.banking.accoutservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccoutserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccoutserviceApplication.class, args);
	}

}
