package com.example.rack_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RackServiceApplication.class, args);
	}

}
