package com.example.descuentoVisitas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DescuentoVisitasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DescuentoVisitasServiceApplication.class, args);
	}

}
