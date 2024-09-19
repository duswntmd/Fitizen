package com.sku.fitizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@EnableScheduling
@SpringBootApplication
public class FitizenApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitizenApplication.class, args);
    }

}
