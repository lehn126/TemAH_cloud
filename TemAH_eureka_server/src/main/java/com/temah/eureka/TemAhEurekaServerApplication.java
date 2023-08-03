package com.temah.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TemAhEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemAhEurekaServerApplication.class, args);
	}

}
