package com.temah.lam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhLamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhLamApplication.class, args);
    }

}
