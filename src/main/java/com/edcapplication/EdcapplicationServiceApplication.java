package com.edcapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {
	    "com.edcapplication.model",
	    "com.empoweredge.model"
	})
@EnableFeignClients(basePackages = "com.edcapplication.client")
public class EdcapplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdcapplicationServiceApplication.class, args);
	}

}
