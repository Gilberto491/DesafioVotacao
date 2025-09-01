package com.sicredi.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SicrediApplication {

    public static void main(String[] args) {
        SpringApplication.run(SicrediApplication.class, args);
    }

}
