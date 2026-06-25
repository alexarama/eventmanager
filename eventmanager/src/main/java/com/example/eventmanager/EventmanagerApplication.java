package com.example.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EventmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}
}