package com.temah.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAdapterApplication.class, args);
    }

}
