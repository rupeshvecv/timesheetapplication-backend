package com.timesheetapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {
	    "com.timesheetapplication.model",
	    "com.empoweredge.model"
	})
@EnableFeignClients(basePackages = "com.timesheetapplication.client")
public class TimesheetapplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetapplicationServiceApplication.class, args);
	}

}
