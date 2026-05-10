package com.sms_microservices.staffProfile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class StaffProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffProfileServiceApplication.class, args);
	}

}
