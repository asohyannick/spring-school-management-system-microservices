package com.sms_microservices.studentProfile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class StudentProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentProfileServiceApplication.class, args);
	}

}
