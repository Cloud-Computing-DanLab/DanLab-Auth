package com.example.dlauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.dlauth.api.service.oauth")
public class DlAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlAuthApplication.class, args);
    }

}
