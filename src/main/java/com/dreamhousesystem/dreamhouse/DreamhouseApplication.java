package com.dreamhousesystem.dreamhouse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class DreamhouseApplication {
	public static void main(String[] args) {
		SpringApplication.run(DreamhouseApplication.class, args);
	}

}
