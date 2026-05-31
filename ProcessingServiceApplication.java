package com.example.processing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProcessingServiceApplication {

	public static void main(String[] args) {


		System.setProperty("server.port", "8071");
		SpringApplication.run(ProcessingServiceApplication.class, args);
	}

}
