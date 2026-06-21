package com.example.khatabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class KhatabackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(KhatabackendApplication.class, args);
    }
}