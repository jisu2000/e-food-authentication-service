package com.subhadeep.e_food_authentication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class EFoodAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EFoodAuthenticationServiceApplication.class, args);
	}

}
