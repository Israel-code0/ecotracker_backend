package com.israel.ecotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcotrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcotrackerApplication.class, args);
    }

}
