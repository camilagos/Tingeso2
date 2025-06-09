package com.example.reporte_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReporteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReporteServiceApplication.class, args);
	}

}
