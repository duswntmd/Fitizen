package com.sku.fitizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class FitizenApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitizenApplication.class, args);
    }

}
